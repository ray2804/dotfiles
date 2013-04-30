
# Non bash-4 method for associative arrays

HOST="https://github.com"
USER="coffeebook"

projects=( "coffeecup:gradus"
           "js2coffee:rstacruz"
           "cs2js:twilson63"
           "coffeecup-helpers:twilson63" )

#
# Main iteration over the pseudo-associative array using
# bash globbing characters to seperate the 'key' from ':'
# the 'value'.
#
for repo in "${projects[@]}"
do $(: a loop over the array of key/value pairs)
  KEY=${repo%%:*}
  ORIG=${repo#*:}
  printf "SUBMODULE: %s/%s/%s.git\n" "$HOST" "$USER" "$KEY"
  printf "UPSTREAMS: %s/%s/%s.git\n" "$HOST" "$ORIG" "$KEY"
  printf '%*s\n' "${COLUMNS:-$(tput cols)}" '' | tr ' ' -
done

