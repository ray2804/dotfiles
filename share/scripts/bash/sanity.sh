
set -o nounset
set -o errexit

# Collection of sanity checks

col=0 # width of terminal window
red=$(tput setaf 1)
green=$(tput setaf 2)
norm=$(tput sgr0)


#
# Pretty prints our sanity check message
#
log_msg() {

    result="$1"; msg="$2" # Function positional arguments to variables

    # Terminal width arithmetic including non-printing character offset
    let col=$(tput cols)-${#msg}+${#green}+${#norm}-2

    # Portable string formatting using terminal capabilities library,
    # tput for colors, positions and cursor placement over 100's of vt's
    printf "%s %s%${col}s" "$green✓$norm" "$msg" "$green[GOOD]$norm"

}


#
# The tests itself
# For easy editing, line is split at the message
#
is_home_writable() { [[ ( -d "$HOME" ) && ( -w "$HOME" ) ]] && log_msg $? "home is a writable directory" ; }

is_root_writable() { [[ ( -d "/" ) && ( -w "/" ) ]] && log_msg $? "root is not a writable directory" ; }



is_home_writable && is_root_writable

#hash foo 2>/dev/null || { echo >&2 "I require foo but it's not installed.  Aborting."; exit 1; }

exit 0