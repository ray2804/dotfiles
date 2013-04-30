

# TODO: Understand fully http://wiki.bash-hackers.org/rcwatson

# Global script variables
host="https://github.com"
user="coffeebook"
here="${PWD}" # to come back when sure all is ok

# Non bash-4 method for associative arrays. Add submodules here that you wish
# remain connected to the upstream original work

# FIXME :: Change this to your collections
projects=( "coffeecup:gradus"
           "js2coffee:rstacruz"
           "cs2js:twilson63"
           "coffeecup-helpers:twilson63"
           "json-template:Gozala")

# NOTES
#XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX
#XXX 1| Bash functions without the 'function' keyword is more portable
#XXX 2| Functions are called, triggered, simply by invoking their names.
#XXX 3| A function call is equivalent to a command given (at the terminal/shell)
#XXX 4| The function def =must= precede first call and can't be declared earlier.
#XXX 5| Alltho you can use them in other functions and call that at the end
#XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX XXX


#XXX Helpers XXX

# These must come first as said so helpers are on top of everything using it
drawline() { printf '\n%*s\n' "${COLUMNS:-$(tput cols)}" '' | tr ' ' - ; }
is_alive() { [[ $(curl -s -o /dev/null -w "%{http_code}" $1) -eq 200 ]] && echo "$green✓$res"; }
temppath() { cd $(mktemp -d); printf "▷ Created %s for temporary storage\n" "$(pwd)"; }
run_cmd(){ local cmd=$1; echo $cmd; printf "%s %s\n" "©" "$cmd" && eval $cmd > /dev/null; }


# Termcap is a library and data base that enables programs to use display
# terminals in a terminal-independent manner and describes the capabilities
# of hundreds of different display terminals in great detail

# Appearance modes
bold=$(tput bold)
u=$(tput smul)
ur=$(tput rmul) # underline reset
red=$(tput setaf 1)
green=$(tput setaf 2)
yellow=$(tput setaf 3)
res=$(tput sgr0) # reset to normal


# Main iterations over a pseudo-associative array using bash globbing patterns
# This will work in ancient terminals/consoles just as well.
fork_print_uri_test()
{
  temppath

  git init

  for repo in "${projects[@]}"
  do $(: a loop over the array of key/value pairs)

    project=${repo%%:*} # chars before the colon *:
    origin=${repo#*:}   # chars after the colon :*

    # Calculate the invisible characters for true width
    let col=$(tput cols)-${#project}+${#green}+${#res}

    # Concatenated strings creates URI
    parent="${host}/${origin}/${project}"
    forked="${host}/${user}/${project}"

    drawline # opening line at every pass full width of terminal

    # Because
    printf "%s%${col}s\n" "$yellow$project$res" "$green[OK]$res"

    # The submodule we want to add is a forked version in our own account.
    printf "%s Submodule: %s.git %s\n" "$(is_alive $forked)" "$forked"

    # The parent project from which we originate our fork
    printf "%s Upstream: %s.git %s\n" "$(is_alive $parent)" "$parent"

    # Create a clone of YOUR forked repo (use the github button first)
    run_cmd 'git clone -v ${forked}'

    # Move into the (by cloning) newly created folder but don't forget return
    # Change directory (cd) of the present/current working directory (PWD/CWD)
    run_cmd 'cd ${project}'

    # Adds 'upstream' (the original) as a new remote to that repository
    run_cmd 'git remote add upstream ${parent}.git'

    # How is this different from just doing a git pull upstream master? This
    # brings in changes from two different sources: his own fork and the main
    # repository (the upstream). Doing a pull usually only brings changes in
    # from one source. If you’re confused about how this works, check out the
    # History section of Git for Computer Scientists for some graphs on how the
    # fetching and merging process works.
    # url:<http://eagain.net/articles/git-for-computer-scientists/#history>
    run_cmd "git fetch origin -v"
    run_cmd "git fetch upstream -v"
    # What about other branches?
    run_cmd "git merge upstream/master"

    # Back to the root path
    cd .. && sleep 5s

  done
  drawline # to close up
}

fork_print_uri_test
exit $?

# Secrets found if you read all the way through this
# In fact, a colon is likewise an acceptable function name.
#:(){ echo ":"; }; :
