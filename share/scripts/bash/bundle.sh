#!/bin/sh

# Do not allow for unbound variables (such as $1) to be used
set -o nounset

# Errors like those above should result in exiting the subshell
set -o errexit

# Collection
MODULE_NAME="bbb" # Bash Badass Bundle


bbb_regex() { gawk 'match($0,/'$1'/, ary) {print ary['${2:-'0'}']}'; }

# show history of installed packages using yaourt with zsh alias 'yainsd'
bbb_yains() { history|grep yainsd|awk ' ($2 ~ /^yainsd/ && !/\-\-/) {print $3 $4} ' | uniq | sort ; }

# show history of installed packages using pacman with flag -S
bbb_pacin() { history|grep pacman|awk ' ($2 ~ /^pacman/ && $3 == "-S" && (! ($4 ~ /-|--/))) {print $4} ' | sort | uniq ; }

bbb_top10() { history | awk '{print $2}' | sort | uniq -c | sort -rn | head ; }
bbb_top20() { history | awk '{print $2}' | sort | uniq -c | sort -rn | head -n 20; }
bbb_least() { history | awk '{print $2}' | sort | uniq -c | sort -rn | tail -n 20; }

# NAIVE power-find commands
#look4() { a=${1:-help}; whence $a; whereis $a; apropos $a; locate $a ; }

# if ethernet nic isnt given, assume 'internet1' as a value (bind variable)
bbb_getip() { /sbin/ifconfig ${1:-internet1} | awk '/inet / {print $2}' ; }

# ${1:-12} means if $1 is unset||null, 12 is returned else val of $1 is replaced
bbb_rndpw() { cat /dev/urandom | tr -cd '[:graph:]' | head -c ${1:-12}  ; }

# clear screen, cursor at top, execute ls, place cursor at the end
bbb_llist() { clear; tput cup 0 0; ls --color=auto -F --color=always -lhFrt; tput cup 40 0 ; }

# display current dir files recursively with full path
bbb_lfull() { find . -type f | sed "s#^.#$(pwd)#" ; }

# lsext displays long list of files of the given extension
bbb_lsext() { find . -type f -iname '*.'${1}'' -exec ls -l {} \; ; }

# displays a tree-like structure using only built-in ls and sed tools ('tree' isnt built-in)
bbb_ltree() { ls -R | grep ":$" | sed -e 's/:$//' -e 's/[^-][^\/]*\//--/g' -e 's/^/   /' -e 's/-/|/' ; }

# remove the leading dot from all files in the current directory using mv, effectively "unhiding" them
bbb_nodot() { find . -maxdepth 1 -type f -name '\.*' | sed -e 's,^\./\.,,' | sort | xargs -iname mv .name name ; }
# NOTE: Probably only works on GNU Linux, due to the specific usage of xargs.

# rename all files in directory to upper case
bbb_ucase() { for i in `ls -1`; do mv $i "${i^^}" ; done ; }

# elegant reverse way to lower case all files in directory
bbb_lcase() { for i in `ls -1`; do mv $i "${i,,}" ; done ; }

# get owner and permissions for directory tree, if not given pwd and default
bbb_todir() { tmp=$(mktemp); find ${1:-`pwd`} -printf "%U %G %m %p\n" > ${2:-$tmp} ; echo $tmp ; }
# TIP: use this twice and compare these against eachother using diff
# http://stackoverflow.com/questions/2792675/how-portable-is-mktemp1

# extract columns from an input stream
#tocol() { awk '{print $('`echo $* | sed -e s/-/NF-/g -e 's/ /'`,$(/g')')\}'; }


