# ~/.bashrc
#
#

# If not running interactively, don't do anything
[[ $- != *i* ]] && return

alias ls='ls --color=auto'
PS1='[\u@\h \W]\$ '

# Ruby Environment Manager
eval "$(rbenv init -)"

#PATH=$PATH:$HOME/.rvm/bin # Add RVM to PATH for scripting
export PATH=$PATH:~/.cabal/bin:~/.xmonad/bin
