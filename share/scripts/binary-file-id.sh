#!/usr/bin/env sh

# Trapping
set -o nounset
set -o errexit

# Variables :: Use parameter expansion for positional parameter $1 or get a
# unbound variable error.
file=${1:-unset}
aan=$(tput setaf 4)
uit=$(tput sgr0)

# Functions
prln() { printf "\n%s\n%s\n" "$aan$1$uit" \
"---------------------------"; }


if [ -f "$file" ]; then

  # Determine type by magic numbers we know
  prln "File type"
  echo "$(tput setaf 1)`file -b $file`$(tput sgr0)"

  # application/octet-stream: Arbitrary binary data.[10] Generally speaking
  # this type identifies files that are not associated with a specific
  # application. Contrary to past assumptions by software packages such as
  # Apache this is not a type that should be applied to unknown files. In such
  # a case, a server or application should not indicate a content type, as it
  # may be incorrect, but rather, should omit the type in order to allow the
  # recipient to guess the type.[11]
  prln "MIME Type"
  echo "$(tput setaf 3)$(file -b -i $file)$(tput sgr0)"

  prln "Hexadecimal"
  hexdump -C $file

  prln "Octal notation"
  od $file

  prln "Octal to hex"
  od -x $file

  prln "Readable strings"
  strings $file

  prln "Formatting hex to shellcodes"
  cat $file | hexdump -v -e '"\\\x" 1/1 "%02x" " "'

  # De meeste shellcode wordt in machinetaal geschreven, de laagste
  # programmeertaal die dan ook het dichtst bij de processor zit.
  # Specialistische kennis over de =instructieset= van een specifieke processor,
  # bijvoorbeeld de x86 processor, is nodig voor het ontwikkelen van shellcode.
  # De instructieset geeft aan, welke commando's door de processor kunnen
  # worden begrepen en uitgevoerd.

  # Een programmeertaal die iets hoger is dan machinetaal is assembleertaal. De
  # binaire instructies van machinetaal, ook wel opcodes genoemd, en die vaak
  # hexadecimaal worden weergegeven, kunnen door een disassembler worden
  # vertaald in assembleertaal. In plaats van de hexadecimale getallen van de
  # machinetaal worden instructies daarin bijvoorbeeld weergegeven als 'xor',
  # 'push' en 'mov', wat voor de meeste mensen leesbaarder is.


  #"Print an 'x' to start, take 1 item / 1 byte, print it as a 2 digit 0 prefixed hex value ('%02X'), and end with a space."
  echo #CAT $FILe | hexdump -v -e '"x" 1/1 "%02x" " "'

  echo #"Print an '[' to start, take 2 items / each 1 byte, print them as a 3 digit 0 prefixed octal value followed by a space ('%03o '), and end with ']\n'."
  echo #cat $file | hexdump -v -e '"[" 2/1 "%03o " "]\n"'
  # NOTE: the trailing space in "%03o " format will not be printed at the end of each interation.

  prln "First 20 hex"
  hexdump -C -n 20 $file

else

  echo
  echo "There is nothing to process! Missing file name."
  exit 3

fi


function print_help() {

cat <<EOF
Argument Description
--------------------

The -v argument is needed to not print duplicate chars as a *. The -e argument
has 5 parts separated by whitespace. Im not sure how multiple format entries
interact.

An optional starting string (in quotes) which is printed before each
conversion. In the first example below we are printing x before each hex
character (x).

An interation count which defaults to 1 if not supplied but has to be supplied
if you want a byte count. This tells how many times to do the conversion before
we print the end string. So if you were decoding 4 things, each of 1 byte,
you would say 4/1.

A byte count which is separated from the interation count by a /. I dont think
there is a way to specify a byte count without an interation count. This
specifies how many bytes are in each item that is being decoded.

A sprintf format. The manual is somewhat useful here. Some standard ones apply
and there are _ extensions. In the first example below this is "%02X" which
prints each byte as a 2 character, 0 prefixed capital hex number.

An optional ending string (in quotes) which is printed after the conversion. A
space in the first example below (" ").

EOF
}


exit 0
