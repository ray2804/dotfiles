#
# Copyright 2012 by Kay-Uwe (Kiwi) Lorenz
# Published under New BSD License, see LICENSE.txt for details.
#

import sublime, sublime_plugin, sys, hashlib, os, glob, json, re
import os.path as P

DEBUG = False
def debug(s, *args):
    if DEBUG:
        sys.stderr.write(s % args)

SUBLIME_DRV = re.compile('^/([A-Z])/')

def get_active_project(window, current_view=None):

    def get_session_file(auto=False):
        if auto:
            ses_file = 'Auto Save Session.sublime_session'
        else:
            ses_file = 'Session.sublime_session'

        settings_dir = os.path.join(sublime.packages_path(), '..', 'Settings')
        window_info = {}

        ses_file = os.path.join(settings_dir, ses_file)
        if os.path.exists(ses_file):
            with open(ses_file, 'r') as input:
                return json.loads(input.read(), strict=False)

        return {}

    ses = get_session_file(auto=True)
    if not ses:
        sys.stderr.write("auto session file does not exist")
        return ""
        #window.ok_cancel_dialog("Auto Save Session File does not exist")

    window_id = window.id()
    for w in ses['windows']:
        if w['window_id'] == window_id:
            project = w['workspace_name']
	    if not project: return ""
            if sublime.platform() == 'windows':
                project =  os.path.normpath(SUBLIME_DRV.sub('\\1:/', project))
            return project

    sys.stderr.write("could not find window id in auto session file")
    return ""
    #WIN_DRV = re.compile('^[A-Za-z]:')


class ProjectSpecific(sublime_plugin.EventListener):
    active = None
    projects = {}
    files = []
    activating = {}
    PROJECT_SPECIFIC_DIR = os.path.join(sublime.packages_path(), 'User', 
		    'Project-Specific')
    current_view = None

    #def __init__(self):
        #self.project_manager = ProjectManager()


    def deactivate(self):
        for f in glob.glob("%s/*.last" % self.PROJECT_SPECIFIC_DIR):
            os.remove(f)

        for f in self.files:
            if P.exists(f):
                os.rename(f, f+'.last')

        self.files = []
        self.active = None


    def do_activate(self, project, settings):
        self.deactivate()

        self.active = project

        prjspec = settings.get('project-specific')
        if not prjspec: return

        prjdir = self.PROJECT_SPECIFIC_DIR
        if not os.path.exists(prjdir):
            os.makedirs(prjdir)

        project_name = os.path.splitext(os.path.basename(project))[0]

        self.files = []
        for k,v in prjspec.items():
            #if k == 'DIRECTORY':
            #    if isinstance(v, basestring):
            #        v = [ v ]
            #    for dir in v:
            #        for 
            if isinstance(v, list):
                if k == 'sublime-keymap':
                    fn = P.join(prjdir, "%s.%s" % ('Default', k))
                else:
                    fn = P.join(prjdir, "%s.%s" % (project_name, k))

                with open(fn, 'wb') as f:
                    json.dump(v, f)

                self.files.append(fn)

            if isinstance(v, dict):
                for xk, xv in v.items():
                    if k == 'sublime-keymap':
                        if 'linux' in xk.lower():
                            fn = P.join(prjdir, "Default (Linux).sublime-keymap")
                        elif 'windows' in xk.lower():
                            fn = P.join(prjdir, "Default (Windows).sublime-keymap")
                        elif 'osx' in xk.lower():
                            fn = P.join(prjdir, "Default (OSX).sublime-keymap")
                        else:
                            fn = P.join(prjdir, "Default.sublime-keymap")
                    else:
                        fn = P.join(prjdir, "%s %s.%s" % (project_name, xk, k))
                    with open(fn, 'wb') as f:
                        json.dump(xv, f)
                    self.files.append(fn)


    def activate(self, view):
        win_id = None
        try:
            window = view.window()
            if not window: return

            win_id = window.id()
            self.activating[win_id] = True

            file_name = view.file_name()

            project = None

            try:
                # sublime text 3
                project = window.project_file_name()
            except AttributeError:
                if file_name is None:
                    pass
                elif file_name.endswith('.sublime-project'):
                    project = view.file_name()
                else:
                    project = get_active_project(window, self.current_view)
    
            if self.current_view:
                window.focus_view(self.current_view)

            if not project:
                if self.active:
                    self.deactivate()

            else:
                if project != self.active:
                    self.do_activate(project, view.settings())
        finally:
            if win_id:
                self.activating[win_id] = False


    def on_activated(self, view):
        if not view.window(): return
        win_id = view.window().id()

        if win_id not in self.activating:
            self.activating[win_id] = False

        if not self.activating[win_id]:
            debug("on_activated: %s\n" % view.file_name())
            sublime.set_timeout(lambda: self.activate(view), 1)

    if 0:
      def on_deactivated(self, view):
        if not view.window(): return
        win_id = view.window().id()

        if win_id not in self.activating:
            self.activating[win_id] = False

        if not self.activating[win_id]:
            self.current_view = view

    def on_post_save(self, view):
        win_id = view.window().id()
        if win_id not in self.activating:
            self.activating[win_id] = False

        if not self.activating[win_id]:
            if view.file_name() == self.active:
                try:
                    self.activating[win_id] = True
                    self.do_activate(self.active, view.settings())
                finally:
                    self.activating[win_id] = False


class ProjectSpecificHelpCommand(sublime_plugin.WindowCommand):

    def run(self, **kargs):
        import webbrowser
        webbrowser.open("https://bitbucket.org/klorenz/projectspecific")

