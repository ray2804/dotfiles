#!/usr/bin/env bash +x
# vim: ft=sh:ts=4:sts=4:sw=4:et:nospell

# Robust portable bash shell script for Literate CoffeeScript
# ===========================================================

# made by =Supersym= on Fri 03 May 2013 08:12:58 PM CEST

# Untangles the .litcoffee file given as the only input parameter to this
# script and both creates the bare-metal `.js` and from this file, round-trip
# to `.coffee` with all text stripped and only code left.

# Syntax highlighting and other tools for Sublime2 and Vim
# https://github.com/jashkenas/coffee-script-tmbundle
# https://github.com/mintplant/vim-literate-coffeescript


# Protect subshell
set -o errexit
set -o nounset

artwork () {
  x="$(tput setaf 1)❤$(tput sgr0)"
  cat <<EOF


             __..._ _...__                 )  (
        _..-"     \`Y\`     "-.._          (   ) )
       \\ Once upon | Literate /           ) ( (
       \\\\  a time, |    $x    //        (_______)_
       \\\\\\  two..  | Coffee ///      .-'---------|
        \\\\\\ _..---.|.---.._ ///     ( C|S\/\\/\\/\\/|
       LP\\\\\`_..---.Y.---.. \`//       '-./\\/\\/\\/\\/|
          '\`               \`'          '_________'
                                        '-------'
EOF
}


# Some eye-candy
norm=$(tput sgr0)
fail=$(tput setaf 1)
good=$(tput setaf 2)
name=$(tput setaf 3)

# Helper functions
die () {  printf >&2 "%s %s\n" "$fail✗$norm Failed! " "$@" && exit 1; }
show () { printf "%s %s\n" "$good✓$norm" "$name$1$norm"; }

# Sanity checks and to limit what we accept as valid input
[ "$#" -eq 1 ] || die "1 argument required, $# provided"

# Member variables for file name can be interpolated now
filename="${1}" # safety first, escape and brace expand properly
basename=$(basename $filename) # strips only path from sanitized name
extension="${filename##*.}" # obtains file extension from the input
shortname="${filename%.*}" # stripped off path and no extension

# Pre-flight final safety checks before take-off
[[ -f "$filename" ]] || die "This is not a actual file you provided."
[[ $extension = "litcoffee" ]] || die "You must provide a .litcoffee file"
[[ $shortname != "" ]] || die "Name can't be a empty string value"

# Showtime!
artwork

# Backup in case something goes wrong
export TMPDIR=$PWD # temporary parent is present working directory
tempfiles=$(mktemp -d -t) # create a tempory folder, works with TMPDIR
cp $filename $tempfiles && cd $tempfiles && show "${filename}" # cp and cd

# All systems are go!
coffee -p -b "${shortname}.litcoffee" > "${shortname}.js" && show "${shortname}.js"
js2coffee "${shortname}.js" > "${shortname}.coffee" && show "${shortname}.coffee"

exit $?

