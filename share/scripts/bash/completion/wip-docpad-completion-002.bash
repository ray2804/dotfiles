# This file is in the public domain.

_arch_compgen() {
  local i r
  COMPREPLY=($(compgen -W '$*' -- "$cur"))
  for ((i=1; i < ${#COMP_WORDS[@]}-1; i++)); do
    for r in ${!COMPREPLY[@]}; do
      if [[ ${COMP_WORDS[i]} = ${COMPREPLY[r]} ]]; then
        unset 'COMPREPLY[r]'; break
      fi
    done
  done
}

_arch_ptr2comp() {
  local list= x y
  for x; do
    for y in '0 --' '1 -'; do
      eval 'set -- ${'$x'[${y% *}]}'
      list+=\ ${@/#/${y#* }}
    done
  done
  _arch_compgen $list
}

_arch_incomp() {
  local r="\s-(-${1#* }\s|\w*${1% *})"; [[ $COMP_LINE =~ $r ]]
}

_pacman_keyids() {
  \pacman-key --list-keys 2>/dev/null | awk '
    $1 == "pub" {
      # key id
      split($2, a, "/"); print a[2]
    }
    $1 == "uid" {
      # email
      if (match($NF, /<[^>]+>/))
        print substr($NF, RSTART + 1, RLENGTH - 2)
    }'
}

_pacman_key() {
  local o cur opts prev wantfiles
  COMPREPLY=()
  _get_comp_words_by_ref cur prev
  opts=('add config delete edit-key export finger gpgdir
         help import import-trustdb init keyserver list-keys list-sigs
         lsign-key nocolor populate recv-keys refresh-keys updatedb
         verify version'
        'a d e f h l r u v V')

  # operations for which we want to complete keyids
  for o in 'd delete' 'e export' 'f finger' 'l list-keys' 'r recv-keys' \
      'edit-key' 'list-sigs' 'lsign-key' 'refresh-keys'; do
    _arch_incomp "$o" && break
    unset o
  done

  # options for which we want file completion
  wantfiles='-@(c|-config|g|-gpgdir)'

  if [[ $prev = 'pacman-key' || ( $cur = -* && $prev != $wantfiles ) ]]; then
    _arch_ptr2comp opts
  elif [[ $prev = @(-k|--keyserver) ]]; then
    return
  elif [[ $prev != $wantfiles && $o ]]; then
    COMPREPLY=($(compgen -W '$(_pacman_keyids)' -- "$cur"))
  fi
  true
}

_makepkg() {
  local cur opts prev
  COMPREPLY=()
  _get_comp_words_by_ref cur prev
  if [[ $cur = -* && ! $prev =~ ^-(-(config|help)$|\w*[Chp]) ]]; then
    opts=('allsource asdeps asroot check clean config force geninteg help holdver ignorearch
           install key log needed nobuild nocheck nocolor noconfirm nodeps noextract
           noprogressbar nosign pkg repackage rmdeps sign skipchecksums skipinteg
           skippgpcheck source syncdeps verifysource version'
          'A L R S c d e f g h i m o p r s')
    _arch_ptr2comp opts
  fi
  true
}

_pacman_pkg() {
  _arch_compgen "$(
    if [[ $2 ]]; then
      \pacman -$1 2>/dev/null | \cut -d' ' -f1 | \sort -u
    else
      \pacman -$1 2>/dev/null
    fi
  )"
}

_pacman() {
  local common core cur database prev query remove sync upgrade o
  COMPREPLY=()
  _get_comp_words_by_ref cur prev
  database=('asdeps asexplicit')
  query=('changelog check deps explicit file foreign groups info list owns
          search unrequired upgrades' 'c e g i k l m o p s t u')
  remove=('cascade dbonly nodeps nosave print recursive unneeded' 'c n p s u')
  sync=('asdeps asexplicit clean dbonly downloadonly force groups ignore ignoregroup
         info list needed nodeps print refresh recursive search sysupgrade'
        'c g i l p s u w y')
  upgrade=('asdeps asexplicit force needed nodeps print recursive' 'p')
  common=('arch cachedir color config dbpath debug help logfile noconfirm
           noprogressbar noscriptlet quiet root verbose' 'b d h q r v')
  core=('database help query remove sync upgrade version' 'D Q R S U V h')

  for o in 'D database' 'Q query' 'R remove' 'S sync' 'U upgrade'; do
    _arch_incomp "$o" && break
  done

  if [[ $? != 0 ]]; then
    _arch_ptr2comp core
  elif [[ ! $prev =~ ^-\w*[Vbhr] &&
    ! $prev = --@(cachedir|color|config|dbpath|help|logfile|root|version) ]]
  then
    [[ $cur = -* ]] && _arch_ptr2comp ${o#* } common ||
      case ${o% *} in
      D|R)
          _pacman_pkg Qq;;
      Q)
        { _arch_incomp 'g groups' && _pacman_pkg Qg sort; }    ||
        { _arch_incomp 'p file'   && _pacman_file; }           ||
          _arch_incomp 'o owns'   || _arch_incomp 'u upgrades' ||
          _pacman_pkg Qq;;
      S)
        { _arch_incomp 'g groups' && _pacman_pkg Sg; }      ||
        { _arch_incomp 'l list'   && _pacman_pkg Sl sort; } ||
          _pacman_pkg Slq;;
      U)
          _pacman_file;;
      esac
  fi
  true
}

_pacman_file() {
  compopt -o filenames; _filedir 'pkg.tar*'
}

complete -F _pacman -o default pacman
complete -F _makepkg -o default makepkg
complete -F _pacman_key -o default pacman-key

# ex:et ts=2 sw=2 ft=sh