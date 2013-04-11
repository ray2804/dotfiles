

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

plugins=(git github archlinux npm coffee cp systemd history git-flow \
  cpanm history-substring-search mvn node ssh-agent sublime rvm \
  gnu-utils)

# Load default (do not change content before this
# Do not modify this file or any inside `~/.oh-my-zsh except custom/`
source $ZSH/oh-my-zsh.sh

# ×º°”˜`”°º× Customize ZSH Below ×º°”`˜”°º×

# Here you CAN override the settings to persist these across updates
unsetopt correct_all    # disable typo auto-correction

# Zshell command found/not-found green/red highlights as in fish shell
source ~/.share/scripts/zsh/plugins/syntax/zsh-syntax-highlighting.zsh

# Custom functions path and allow recursive search
fpath=(
  ~/.share/scripts/zsh/autoloaded
  ~/.share/scripts/zsh/autoloaded/zsh-completions/src
  ~/.share/scripts/zsh/autoloaded/**/*~*/(CVS)#(/N)
  "${fpath[@]}"
  )



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

############### ############### ###############
## Search paths ##

# Keep /usr/bin first (or might get confused)
export PATH=/var/tmp/bin:/usr/bin:/usr/local/bin:/bin:/usr/local/sbin:/usr/sbin:/sbin

# Append to path user profile bin folders (suffixes will do)
export PATH="${PATH}:${HOME}/bin"

# Prefix path with symlinks in colorgcc bin to enable color compile
export PATH="/usr/lib/colorgcc/bin:${PATH}"

# Build tools .............................................................
export PATH="/opt/maven2/bin:${PATH}"


############### ############### ###############
# Polyglot IDE ##
############### ############### ###############

# Cross-compiler tools embedded clfs cross-linux-from-scratch .............
#export CLFS=/mnt/clfs

# Node.js (NPM) ...........................................................
#
export NODE_DIR="/usr/local/lib"
export NODE_PATH="/usr/local/lib/node_modules"
#export PATH="${PATH}:node_modules/.bin"

# Haskell .................................................................
#
export PATH="${PATH}:${HOME}/.cabal/bin"

# Perl (CPAN) .............................................................
#
export PATH="${PATH}:/usr/bin/core_perl"

# Java (JRE/JDK) ..........................................................
#
export JAVA_HOME=${JAVA_HOME:-/usr/lib/jvm/java-7-openjdk/jre}

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

export PATH=$PATH:$HOME/.rvm/gems/ruby-2.0.0-p0@global/bin:$HOME/.rvm/rubies/ruby-2.0.0-p0/bin:$HOME/.rvm/bin:/opt/maven2/bin:/usr/lib/colorgcc/bin:$HOME/Dotfiles/bin


#[[ "$PS1" ]] && {
#    mkdir -m 0700 /sys/fs/cgroup/cpu/user/$$;
#    echo $$ > /sys/fs/cgroup/cpu/user/$$/tasks; 
#}

