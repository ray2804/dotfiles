



# Preamble .................

# Early load sequences

# These allow you to execute commands at the beginning of the X user session,
# before the Window Manager is started - ensure it is placed before the wm 'exec'
[[ -f /etc/xprofile ]] && . /etc/xprofile
[[ -f ~/.xprofile ]] && . ${HOME}/.xprofile

#
# General preferences
#

# System bell == hate
xset -b

# FIXME Enable faster repeat for - and ESC
xset r rate 190 40

# Fix for Xcursor mouse pointers; also in `.Xresources` (xrdb) but it doesn't
# work in every window such as on the root desktop (wallpaper) it would show a
# cross. This fixes it. Don't forget the & sign to fork over the new session.
xsetroot -cursor_name left_ptr &
#ComixCursors-Red-Large

# Fix stupid Java bug where it doesn't seem to like Awesome WM etc.
# Disabled: xmonad does this in xmonad.hs
#wmname "LG3D"


# Resources and WM/X11 assets .......................................

# Paradigm of local user and system-wide userland,
# in a concrete implementation of xserver defaults
userresources=${HOME}/.Xresources
usermodmap=${HOME}/.Xmodmap
sysresources=/etc/X11/xinit/Xresources
sysmodmap=/etc/X11/xinit/Xmodmap

# Merge system-wide & user resources and keymaps all neatly in one.
[[ -r "${sysresources}" ]]  && xrdb -merge "${sysresources}"
[[ -r "${sysmodmap}" ]]     && xmodmap "${sysmodmap}"
[[ -r "${userresources}" ]] && xrdb -merge "${userresources}"
[[ -r "${usermodmap}" ]]    && xmodmap "${usermodmap}"
[[ -f "${oldconfig}" ]]     && xrdb -merge "${oldconfig}"

# Start compositing window manager
#xcompmgr -c &
#export DE='xfce'

# Set screen displays (done in /etc/xprofile now)
#xrandr --output VGA-1 --off --output DVI-I-1 --mode 1600x900 --pos 1920x168 --rotate normal --output HDMI-1 --mode 1920x1200 --pos 0x0 --rotate normal


# Compositing window manager
#xcompmgr -c -t-5 -l-5 -r4.2 -o.55 &

# Start conky infolet
conky &

# Set transp. in all wm
#transset-df .5 -n Conky

# Desktop wallpaper
sh ${HOME}/.fehbg &

# Spawn unicode rxvt daemon or client
#urxvtcd &
#terminator &

# Fire up the window manager with SSH ident.
exec ssh-agent xmonad
#exec gnome-session --session xmonad-gnome-nopanel "$@"




