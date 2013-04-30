# https://groups.google.com/d/msg/nodejs/GGL-SGfWKkI/5fjxogTydYUJ
# http://docpad.org/docs/config
# http://www.hacksparrow.com/running-express-js-in-production-mode.html

# Keep all projects here?
DIR_PROJ="~/projects/docpad"


_dpad() 
{
    local cur prev opts
    COMPREPLY=()
    cur="${COMP_WORDS[COMP_CWORD]}"
    prev="${COMP_WORDS[COMP_CWORD-1]}"
    opts="playground debugging testing production development experimental"

    if [[ ${cur} == -* ]] ; then
        COMPREPLY=( $(compgen -W "${opts}" -- ${cur}) )
        return 0
    fi
}
complete -F _dpad dpad


