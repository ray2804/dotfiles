
autoload termcolors

alias dpg="docpad generate"
alias dpgstat="docpad generate --env static"
alias dpgbug="docpad generate -d|grep -i warning|less"
alias dpskel="docpad skeleton"
alias dpc2j="{cson2json package.cson | json >> package2.json} || echo 'Something went wrong...' "
alias dphub="docpad deploy-ghpages || echo 'Could not deploy. Do you have the plugin?'"
alias dpc="docpad clean"
alias dpr="docpad run"

# Usage: docpad [options] [command]

  #Commands:

    #action <actions>       does everyting: skeleton, generate, watch, server
    #run                    does everyting: skeleton, generate, watch, server
    #server                 creates a server for your generated project
    #skeleton [options]     will create a new project in your cwd based off an existing skeleton
    #render [path]          render the file at <path> and output its results to stdout
    #generate               (re)generates your project
    #watch                  watches your project for changes, and (re)generates whenever a change is made
    #install                ensure everything is installed correctly
    #clean                  ensure everything is cleaned correctly (will remove your out directory)
    #info                   display the information about your docpad instance
    #help                   output the help
    #*                      anything else ouputs the help

  #Options:

    #-h, --help                 output usage information
    #-V, --version              output the version number
    #-o, --out <outPath>        where to output the rendered files (to a directory) or file (to an output file)
    #-c, --config <configPath>  a custom configuration file to load in
    #-e, --env <environment>    the environment name to use for this instance, multiple names can be separated with a comma
    #-d, --debug [logLevel]     the level of debug messages you would like to display, if specified defaults to 7, otherwise 6
    #-f, --force                force a re-install of all modules
    #-p, --port <port>          a custom port to use for the server <port>
    #-s, --skeleton <skeleton>  for new projects, instead of being asked for the skeleton, you can specify it here

