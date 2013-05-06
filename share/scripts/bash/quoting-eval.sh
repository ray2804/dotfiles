#!/bin/sh

# Shell protection
#
# Do not allow unbound use of variables
# Bind variable '!conf' by appending :- to the end
set -o nounset
# Exit subshell environment on any errors like above
set -o errexit


for conf in HD CDR SWAP_SIZE CONF_URL
do
  :
  echo -n "$conf: (default = ${!conf:-}): "
  read tempvar
  [[ ! -z "$tempvar" ]] && eval ${conf}=\$tempvar

done
