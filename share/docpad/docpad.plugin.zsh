#!zsh
#
# Installation
# ------------
#
# To achieve git-flow completion nirvana:
#
#  0. Update your zsh's git-completion module to the newest verion.
#     From here. http://zsh.git.sourceforge.net/git/gitweb.cgi?p=zsh/zsh;a=blob_plain;f=Completion/Unix/Command/_git;hb=HEAD
#
#  1. Install this file. Either:
#
#     a. Place it in your .zshrc:
#
#     b. Or, copy it somewhere (e.g. ~/.git-flow-completion.zsh) and put the following line in
#        your .zshrc:
#
#            source ~/.git-flow-completion.zsh
#
#     c. Or, use this file as a oh-my-zsh plugin.
#

_git-flow ()
{
  local curcontext="$curcontext" state line
  typeset -A opt_args

  _arguments -C \
    ':command:->command' \
    '*::options:->options'

  case $state in
    (command)

      local -a subcommands
      subcommands=(
        'action:[action] performs one or more actions e.g. docpad action clean,generate'
        'run:does everyting (skeleton, generate, watch, server)'
        'server:creates a server for your generated project'
        'skeleton:[options] will create a new project in your cwd based off an existing skeleton'
        'render:[path] render the file at <path> and output its results to stdout'
        'generate:(re)generates your project'
        'watch:watches your project for changes, and (re)generates whenever a change is made'
        'install:ensure everything is installed correctly'
        'clean:ensure everything is cleaned correctly (will remove your out directory)'
        'info:display the information about your docpad instance'
        'help:output the help'
      )
      _describe -t commands 'docpad subcommands' subcommands
    ;;

    (options)
      case $line[1] in

        (action)
            __docpad-action
          #_arguments \
          #  -f'[Force setting of gitflow branches, even if already configured]'
          ;;

          (version)
          ;;

          (hotfix)
            __git-flow-hotfix
          ;;

          (release)
            __git-flow-release
          ;;

          (feature)
            __git-flow-feature
          ;;
      esac
    ;;
  esac
}

__docpad-action ()
{
  local curcontext="$curcontext" state line
  typeset -A opt_args

  _arguments -C \
    ':command:->command' \
    '*::options:->options'

  case $state in
    (command)

      local -a subcommands
      subcommands=(
        'clean:ensure everything is cleaned correctly (will remove your out directory)'
        'run:does everyting (skeleton, generate, watch, server)'
        'server:creates a server for your generated project'
        'skeleton:[options] will create a new project in your cwd based off an existing skeleton'
        'generate:(re)generates your project'
        'watch:watches your project for changes, and (re)generates whenever a change is made'
      )
      _describe -t commands 'docpad actions' subcommands
      _arguments \
        -v'[Verbose (more) output]'
    ;;

    (options)
      case $line[1] in

        (start)
#          _arguments \
            #-F'[Fetch from origin before performing finish]'\
            #':version:__git_flow_version_list'
        ;;

        (finish)
          _arguments \
            -F'[Fetch from origin before performing finish]' \
            -s'[Sign the release tag cryptographically]'\
            -u'[Use the given GPG-key for the digital signature (implies -s)]'\
            -m'[Use the given tag message]'\
            -p'[Push to $ORIGIN after performing finish]'\
            -k'[Keep branch after performing finish]'\
            -n"[Don't tag this release]"\
            ':version:__git_flow_version_list'
        ;;

        *)
          _arguments \
            -v'[Verbose (more) output]'
        ;;
      esac
    ;;
  esac
}

__git-flow-hotfix ()
{
  local curcontext="$curcontext" state line
  typeset -A opt_args

  _arguments -C \
    ':command:->command' \
    '*::options:->options'

  case $state in
    (command)

      local -a subcommands
      subcommands=(
        'start:Start a new hotfix branch.'
        'finish:Finish a hotfix branch.'
        'list:List all your hotfix branches. (Alias to `git flow hotfix`)'
      )
      _describe -t commands 'git flow hotfix' subcommands
      _arguments \
        -v'[Verbose (more) output]'
    ;;

    (options)
      case $line[1] in

        (start)
          _arguments \
            -F'[Fetch from origin before performing finish]'\
            ':hotfix:__git_flow_version_list'\
            ':branch-name:__git_branch_names'
        ;;

        (finish)
          _arguments \
            -F'[Fetch from origin before performing finish]' \
            -s'[Sign the release tag cryptographically]'\
            -u'[Use the given GPG-key for the digital signature (implies -s)]'\
            -m'[Use the given tag message]'\
            -p'[Push to $ORIGIN after performing finish]'\
            -k'[Keep branch after performing finish]'\
            -n"[Don't tag this release]"\
            ':hotfix:__git_flow_hotfix_list'
        ;;

        *)
          _arguments \
            -v'[Verbose (more) output]'
        ;;
      esac
    ;;
  esac
}

__git-flow-feature ()
{
  local curcontext="$curcontext" state line
  typeset -A opt_args

  _arguments -C \
    ':command:->command' \
    '*::options:->options'

  case $state in
    (command)

      local -a subcommands
      subcommands=(
        'start:Start a new feature branch.'
        'finish:Finish a feature branch.'
        'list:List all your feature branches. (Alias to `git flow feature`)'
        'publish: publish'
        'track: track'
        'diff: diff'
        'rebase: rebase'
        'checkout: checkout'
        'pull: pull'
      )
      _describe -t commands 'git flow feature' subcommands
      _arguments \
        -v'[Verbose (more) output]'
    ;;

    (options)
      case $line[1] in

        (start)
          _arguments \
            -F'[Fetch from origin before performing finish]'\
            ':feature:__git_flow_feature_list'\
            ':branch-name:__git_branch_names'
        ;;

        (finish)
          _arguments \
            -F'[Fetch from origin before performing finish]' \
            -r'[Rebase instead of merge]'\
            -k'[Keep branch after performing finish]'\
            ':feature:__git_flow_feature_list'
        ;;

        (publish)
          _arguments \
            ':feature:__git_flow_feature_list'\
        ;;

        (track)
          _arguments \
            ':feature:__git_flow_feature_list'\
        ;;

        (diff)
          _arguments \
            ':branch:__git_flow_feature_list'\
        ;;

        (rebase)
          _arguments \
            -i'[Do an interactive rebase]' \
            ':branch:__git_flow_feature_list'
        ;;

        (checkout)
          _arguments \
            ':branch:__git_flow_feature_list'\
        ;;

        (pull)
          _arguments \
            ':remote:__git_remotes'\
            ':branch:__git_flow_feature_list'
        ;;

        *)
          _arguments \
            -v'[Verbose (more) output]'
        ;;
      esac
    ;;
  esac
}

__git_flow_version_list ()
{
  local expl
  declare -a versions

  versions=(${${(f)"$(_call_program versions git flow release list 2> /dev/null | tr -d ' |*')"}})
  __git_command_successful || return

  _wanted versions expl 'version' compadd $versions
}

__git_flow_feature_list ()
{
  local expl
  declare -a features

  features=(${${(f)"$(_call_program features git flow feature list 2> /dev/null | tr -d ' |*')"}})
  __git_command_successful || return

  _wanted features expl 'feature' compadd $features
}

__git_remotes () {
  local expl gitdir remotes

  gitdir=$(_call_program gitdir git rev-parse --git-dir 2>/dev/null)
  __git_command_successful || return

  remotes=(${${(f)"$(_call_program remotes git config --get-regexp '"^remote\..*\.url$"')"}//#(#b)remote.(*).url */$match[1]})
  __git_command_successful || return

  # TODO: Should combine the two instead of either or.
  if (( $#remotes > 0 )); then
    _wanted remotes expl remote compadd $* - $remotes
  else
    _wanted remotes expl remote _files $* - -W "($gitdir/remotes)" -g "$gitdir/remotes/*"
  fi
}

__git_flow_hotfix_list ()
{
  local expl
  declare -a hotfixes

  hotfixes=(${${(f)"$(_call_program hotfixes git flow hotfix list 2> /dev/null | tr -d ' |*')"}})
  __git_command_successful || return

  _wanted hotfixes expl 'hotfix' compadd $hotfixes
}

__git_branch_names () {
  local expl
  declare -a branch_names

  branch_names=(${${(f)"$(_call_program branchrefs git for-each-ref --format='"%(refname)"' refs/heads 2>/dev/null)"}#refs/heads/})
  __git_command_successful || return

  _wanted branch-names expl branch-name compadd $* - $branch_names
}

__git_command_successful () {
  if (( ${#pipestatus:#0} > 0 )); then
    _message 'not a git repository'
    return 1
  fi
  return 0
}

zstyle ':completion:*:*:git:*' user-commands flow:'description for foo'


