import re
import os
import sys
import subprocess
import signal
import threading
import time
import ltmain

def noop():
  pass

ipy = None
respond = None
requests = {}
connected = noop
disconnected = noop

def km_from_string(s=''):
    from os.path import join as pjoin
    from IPython.zmq.blockingkernelmanager import BlockingKernelManager, Empty
    from IPython.config.loader import KeyValueConfigLoader
    from IPython.zmq.kernelapp import kernel_aliases
    global km,send,Empty

    s = s.replace('--existing', '')
    from IPython.lib.kernel import find_connection_file
    # 0.12 uses files instead of a collection of ports
    # include default IPython search path
    # filefind also allows for absolute paths, in which case the search
    # is ignored
    try:
      # XXX: the following approach will be brittle, depending on what
      # connection strings will end up looking like in the future, and
      # whether or not they are allowed to have spaces. I'll have to sync
      # up with the IPython team to address these issues -pi
      if '--profile' in s:
        k,p = s.split('--profile')
        k = k.lstrip().rstrip() # kernel part of the string
        p = p.lstrip().rstrip() # profile part of the string
        fullpath = find_connection_file(k,p)
      else:
        fullpath = find_connection_file(s.lstrip().rstrip())
    except IOError as e:
        return
    km = BlockingKernelManager(connection_file = fullpath)
    km.load_connection_file()
    km.start_channels()
    send = km.shell_channel.execute
    return km

def _extract_traceback(traceback):
  # strip ANSI color controls
  strip = re.compile('\x1B\[([0-9]{1,2}(;[0-9]{1,2})?)?[m|K]')
  return [strip.sub('',t) for t in traceback]

def msgId(m):
  if 'parent_header' in m and 'msg_id' in m['parent_header']:
    return m['parent_header']['msg_id']

def normalize(m):
  mid = msgId(m)
  content = m['content']

  data = {}
  if 'data' in content:
    data = m['content']['data']

  if 'text/plain' in data:
    return {'mid': mid, 'type': 'return', 'data': data['text/plain']}
  if 'image/png' in data:
    return {'mid': mid, 'type': 'image', 'data': data['image/png']}
  if 'traceback' in content:
    return {'mid': mid, 'type': 'ex', 'data': "\n".join(_extract_traceback(content['traceback']))}
  if 'name' in content and content['name'] == 'stdout':
    return {'mid': mid, 'type': 'out', 'data': data}
  if 'name' in content and content['name'] == 'stderr':
    return {'mid': mid, 'type': 'err', 'data': data}
  if 'execution_state' in content and content['execution_state'] == 'idle':
    return {'mid': mid, 'type':'done'}

  content['type'] = "unknown"
  content['mid'] = mid
  return content

def msgs():
  global km
  msgs = km.sub_channel.get_msgs()
  return [normalize(i) for i in msgs]

def handleMsg(m):
  if not m['mid']:
    return

  try:
    orig = requests[m['mid']]
  except:
    return

  command = None
  ret = {'meta': orig['meta']}

  if m['type'] == 'return':
    orig['return'] = True
    command = 'editor.eval.python.result'
    ret['result'] = m['data']
  elif m['type'] == 'ex':
    orig['return'] = True
    command = 'editor.eval.python.exception'
    ret['ex'] = m['data']
  elif m['type'] == 'image':
    command = 'editor.eval.python.image'
    ret['image'] = m['data']
  elif m['type'] == 'out':
    command = 'editor.eval.python.print'
    ret['file'] = orig['path']
    ret['msg'] = m['data']
  elif m['type'] == 'done' and orig['evaltype'] == 'statement' and 'return' not in orig:
    orig['return'] = True
    command = 'editor.eval.python.success'
  elif m['type'] == 'done' and orig['evaltype'] == 'expression' and 'return' not in orig:
    orig['return'] = True
    command = 'editor.eval.python.result'
    ret['result'] = 'None'

  if command:
    respond(orig["client"], command, ret)

def msgloop():
  global ipy
  while not ipy.returncode and not ltmain.stopped():
    for m in msgs():
      handleMsg(m)
    time.sleep(.01)

def initIPy(s):
  try:
    global km
    km = km_from_string(s)
    loc = os.path.dirname(__file__)
    send("import sys\nsys.path.append('" + loc.replace('\\','\\\\') + "')\nimport lttools")
    send("%config InlineBackend.close_figures=False")
    connected()
    msgloop()
    disconnected()
  except:
    disconnected()

def setNs(path):
  send("import lttools\nlttools.switch_ns('" + path.replace('\\', '\\\\') + "')")

def IPyOutput(l):
  m = re.search('--existing (.*\.json)', l)
  if m:
    initIPy(m.group(1))
  if re.search('ImportError: IPython.zmq', l):
    respond(None, "python.client.error.pyzmq", None)

def listenIPy():
  global ipy
  while True:
    #next_line = proc.communicate()[0]
    next_line = ipy.stdout.readline().decode('utf-8')
    if next_line == '' and ipy.poll() != None:
        disconnected()
        break
    IPyOutput(next_line)

def startIPy(opts):
  global ipy
  global respond
  global disconnected
  global connected
  respond = opts["respond"]
  connected = opts["connected"]
  disconnected = opts["disconnected"]

  try:
    if os.path.isfile('bin/ipython'):
      cmd = 'bin/ipython'
    else:
      cmd = 'ipython'
    ipy = subprocess.Popen([cmd, 'kernel', '--pylab=inline'], stdout=subprocess.PIPE, stderr=subprocess.STDOUT, env=os.environ)
    #Start a thread listening to stdout
    t = threading.Thread(target=listenIPy)
    t.start()
    return True
  except:
    disconnected()
    return None

def killIPy():
  global ipy
  try:
    send('exit')
    ipy.terminate()
    ipy.returncode = True
  except:
    pass

def request(cur):
  id = send(cur["code"])
  requests[id] = cur
