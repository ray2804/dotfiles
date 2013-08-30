import sublime_plugin


class ExpandSelectionToOtherLinesCommand(sublime_plugin.TextCommand):
    def run(self, edit):
        self.view.window().run_command("expand_selection", {"to": "line"})
        start_region = self.view.sel()[0]
        self.view.window().run_command("select_all")
        self.view.sel().subtract(start_region)