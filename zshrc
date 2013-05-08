

# (¯`·._(¯`·._(¯`·._( ZSH )_.·´¯)_.·´¯)_.·´¯)


# Compsys initialization
autoload -U compinit
compinit

# Show completion menu when number of options is at least 2
zstyle ':completion:*' menu select=2

# Path to your oh-my-zsh configuration.
ZSH="${HOME}/.oh-my-zsh"

# Set this to "random" for a new theme every time
#ZSH_THEME="afowler 3den"
ZSH_THEME="af-magic"
#ZSH_THEME="alanpeabody"
#ZSH_THEME="dieter"
#ZSH_THEME="eastwood"
#ZSH_THEME="gallois"
#ZSH_THEME="fishy"
#ZSH_THEME="miloshadzic"
#ZSH_THEME="mrtazz"
#ZSH_THEME="terminalparty"
#ZSH_THEME="muse"


plugins=(git github archlinux npm coffee cp systemd history git-flow \
  cpanm history-substring-search mvn node ssh-agent sublime rvm \
  gnu-utils)

# Load default (do not change content before this
# Do not modify this file or any inside `~/.oh-my-zsh except custom/`
source $ZSH/oh-my-zsh.sh

# ×º°”˜`”°º× Customize ZSH Below ×º°”`˜”°º×

# Here you CAN override the settings to persist these across updates
unsetopt correct_all    # disable typo auto-correction

# AUTO_TITLE: parameter not set error is prevent with autonaming windows
# http://superuser.com/questions/306028/tmux-and-zsh-custom-prompt-bug-with-window-name
DISABLE_AUTO_TITLE=true


# Zshell command found/not-found green/red highlights as in fish shell
source ~/.share/scripts/zsh/plugins/syntax/zsh-syntax-highlighting.zsh

# Custom functions path and allow recursive search
fpath=(
  ~/.share/scripts/zsh/autoloaded
  ~/.share/scripts/zsh/autoloaded/zsh-completions/src
  ~/.share/scripts/zsh/autoloaded/**/*~*/(CVS)#(/N)
  "${fpath[@]}"
  )


# Alt-S inserts "sudo " at the start of line:
insert_sudo () { zle beginning-of-line; zle -U "sudo " }
zle -N insert-sudo insert_sudo
bindkey "^[n" insert-sudo

# Meta-u to chdir to the parent directory
bindkey -s '\eu' '^Ucd ..; ls^M'

# If AUTO_PUSHD is set, Meta-p pops the dir stack
bindkey -s '\ep' '^Upopd >/dev/null; dirs -v^M'

# Pipe the current command through less
bindkey -s "\el" " 2>&1|less^M"



# Term
[[ -s $HOME/.tmuxinator/scripts/tmuxinator ]] &&\
  source $HOME/.tmuxinator/scripts/tmuxinator



# (¯`·._(¯`·._(¯`·._( Desktop )_.·´¯)_.·´¯)_.·´¯)


export GTK_IM_MODULE=ibus
export XMODIFIERS=@im=ibus
export QT_IM_MODULE=ibus

export AURLINKS=comments


# Logs and log tools
#export SPLUNK_HOME="/opt/splunk"
#export PATH="${PATH}:${SPLUNK_HOME}/bin"

# Compilation from source without making a mess
#
#  ./configure --prefix=$STOW/appname
#  make && make install prefix=$STOW/appname
#  cd $STOW && stow appname
#
# Uninstallation is as simple as:
#
#  cd $STOW && stow -D appname
#export STOW=/usr/local/stow

# Explicit language since some apps need it
export LANG=en_US.UTF-8

# Pager keep info on screen after exit
export LESS=X

# Default text editor (also used in ranger preview)
export EDITOR=vim

# Very fast web browser, lightweight and highly customizable
export BROWSER=dwb

# Use less to view man files etc
export PAGER=less


# Paths

# Keep /usr/bin first (or might get confused)
export PATH=/var/tmp/bin:/usr/bin:/usr/local/bin:/bin:/usr/local/sbin:/usr/sbin:/sbin

# Append to path user profile bin folders (suffixes will do)
export PATH="${PATH}:${HOME}/bin"

# Prefix path with symlinks in colorgcc bin to enable color compile
export PATH="/usr/lib/colorgcc/bin:${PATH}"

# Build tools .............................................................
export PATH="/opt/maven2/bin:${PATH}"




# -----------------------------------------------------------
# AUTOCOMPLETE TWEAKS
unsetopt menu_complete   # do not autoselect the first completion entry
unsetopt flowcontrol
setopt auto_menu         # show completion menu on succesive tab press
setopt complete_in_word
setopt always_to_end
zstyle ':completion:*' add-space true
zstyle ':completion:*' completer _expand _complete _match _prefix _approximate _list
zstyle ':completion:*' menu select=1
zstyle ':completion:*' file-sort name
zstyle ':completion:*' list-colors ${(s.:.)ZLS_COLORS}
zstyle ':completion:*' matcher-list 'm:{a-z}={A-Z}' 'r:|[._-]=** r:|=**' 'l:|=** r:|=**'
#zstyle ':completion:*' matcher-list 'r:|[._-]=** r:|=**' 'l:|=** r:|=**'
zstyle ':completion:*' menu select
zstyle ':completion:*:approximate:*' max-errors 'reply=( $(( ($#PREFIX+$#SUFFIX)/3 )) numeric )'
zstyle ':completion:*' group 1
zstyle ':completion:*' format '%B---- %d%b'
zstyle ':completion:*:corrections' format '%B---- %d (errors %e)%b'
zstyle ':completion:*:descriptions' format "%B---- %d%b"
zstyle ':completion:*:messages' format '%B%U---- %d%u%b'
zstyle ':completion:*:warnings' format "%B$fg[red]%}---- no match for: $fg[white]%d%b"
zstyle ':completion:*' group-name ''
# -----------------------------------------------------------




############### ############### ###############
# Polyglot IDE ##
############### ############### ###############

# Cross-compiler tools embedded clfs cross-linux-from-scratch .............
#export CLFS=/mnt/clfs

# Node.js (NPM) ...........................................................
#
export NODE_DIR="/usr/local/bin"
export NODE_PATH="/usr/local/lib/node_modules"
#export PATH="${PATH}:node_modules/.bin"

# Haskell .................................................................
#
export PATH="${PATH}:${HOME}/.cabal/bin"

# Coffee
#
export CONTRACTS_COFFEE_ENABLED=1

# Perl (CPAN) .............................................................
#
export PATH="${PATH}:/usr/bin/core_perl"

# Java (JRE/JDK) ..........................................................
#
#export JAVA_HOME=${JAVA_HOME:-/usr/lib/jvm/java-7-openjdk/jre}
#export JAVA_HOME=${JAVA_HOME:/usr/lib/jvm/java-7-openjdk/jre/bin/java}
#export PATH=$PATH:/usr/lib/jvm/java-7-openjdk/jre/bin

# Enable this for non-reparenting window managers
# #export _JAVA_AWT_WM_NONREPARENTING=1

# Ruby (RVM) ..............................................................
#
export PATH="${PATH}:${HOME}/.rvm/bin:${GEM_PATH}/bin"
[[ -f ~/.rvm/scripts/rvm ]] && source ~/.rvm/scripts/rvm

###############################################
##                                           ##
##          Sourced shell scripts            ##
##                                           ##
###############################################
source ${HOME}/.aliases
#source ${HOME}/.welcome

# Bash bundle
#. .share/scripts/bash/bundle.sh


export PATH=$PATH:$HOME/.rvm/gems/ruby-2.0.0-p0@global/bin:$HOME/.rvm/rubies/ruby-2.0.0-p0/bin:$HOME/.rvm/bin:/opt/maven2/bin:/usr/lib/colorgcc/bin:$HOME/Dotfiles/bin


#[[ "$PS1" ]] && {
#    mkdir -m 0700 /sys/fs/cgroup/cpu/user/$$;
#    echo $$ > /sys/fs/cgroup/cpu/user/$$/tasks; 
#}

