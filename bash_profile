#
# ~/.bash_profile
#

[[ -f ~/.bashrc ]] && . ~/.bashrc

[[ -s "$HOME/.rvm/scripts/rvm" ]] && source "$HOME/.rvm/scripts/rvm" # Load RVM into a shell session *as a function*
export CLOJURESCRIPT_HOME=/usr/local/src/clojurescript
export PATH=$PATH:/usr/local/src/clojurescript/bin
