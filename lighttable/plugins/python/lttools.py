import os
import sys
import site
from os import environ
from os.path import join
from sys import version_info

ipy = sys.modules['__main__'].__dict__['get_ipython']()

def toModuleNameByPath(path):
  cur = [os.path.splitext(os.path.basename(path))[0]]
  p = os.path.dirname(path);
  while os.path.exists(os.path.join(p, "__init__.py")):
    cur.insert(0, os.path.basename(p))
    p = os.path.dirname(p)
  return ".".join(cur)

def toModule(path):
  name = toModuleNameByPath(path)
  if name in sys.modules:
    return sys.modules[name]
  else:
    parts = name.split(".")
    for idx in range(len(parts)):
      mname = ".".join(parts[:idx+1])
      __import__(mname)
    return sys.modules[name]

def switch_ns(path):
  module = toModule(path)
  ipy.init_create_namespaces(module)
  ipy.init_user_ns()

#----------------------------------------------------------
# VirtualEnv magic
#----------------------------------------------------------
if 'VIRTUAL_ENV' in environ:
    virtual_env = join(environ.get('VIRTUAL_ENV'),
                       'lib',
                       'python%d.%d' % version_info[:2],
                       'site-packages')
    site.addsitedir(virtual_env)
    del virtual_env
del site, environ, join, version_info
#----------------------------------------------------------
