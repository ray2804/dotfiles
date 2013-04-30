
The Basic Mechanics of the Completion System
--------------------------------------------
<http://www.linux-mag.com/id/1106/>

If you look at the actual source of the completion functions (by searching
through the directories in $fpath), you might notice how often the _arguments()
function is used. It’s everywhere because it’s the most important function
authors of completion functions use.

The _arguments() function is actually a wrapper around the compadd() built-in
function. It’s compadd() that ultimately takes information and feeds it to the
core of the completion system. Another important function to be aware of is
compdef() which is used to bind completion functions to commands.i

``` sh

#compdef figlet

typeset -A opt_args

local context state line

local fontdir

fontdir=$(_call_program path figlet -I2 2>/dev/null)



_arguments -s -S \

"(-l -c -r)-x[use default justification of font]" \

"(-x -c -r)-l[left justify]" \

"(-x -l -r)-c[center justify]" \

"(-x -l -c)-r[right justify]" \

"(-S -s -o -W -m)-k[use kerning]" \

"(-k -s -o -W -m)-S[smush letters together or else!]" \

"(-k -S -o -W -m)-s[smushed spacing]" \

"(-k -S -s -W -m)-o[let letters overlap]" \

"(-k -S -s -o -m)-W[wide spacing]" \

"(-p)-n[normal mode]" \

"(-n)-p[paragraph mode]" \

"(-E)-D[use Deutsch character set]" \

"(-D)-E[use English character set]" \

"(-X -R)-L[left-to-right]" \

"(-L -X)-R[right-to-left]" \

"(-L -R)-X[use default writing direction of font]" \

"(-w)-t[use terminal width]" \

"(-t)-w+[specify output width]:output width (in columns):" \

"(-k -S -s -o -W)-m+[specify layout mode]:layout mode:" \

"(-I)-v[version]" \

"(-v)-I+[display info]:info code:(-1 0 1 2 3 4)" \

"-d+[specify font directory]:font directory:_files -/" \

"-f+[specify font]:font:->fonts" \

"(-N)-C+[specify control file]:control file:->controls" \

"(-C)-N[clear controlfile list]" \

&& return 0



(( $+opt_args[-d] )) && fontdir=$opt_args[-d]



case $state in

	(fonts)

	_files -W $fontdir -g ‘*flf*(:r)’ && return 0

	;;

	(controls)

	_files -W $fontdir -g ‘*flc*(:r)’ && return 0

	;;

esac



return 1

```

To use this function, store it in a file called _figlet and place it somewhere
in your $fpath. If you don’t w ant (or don’t have the permission) to add files
into a directory in $fpath, you can create a directory in your home directory
(for example, fun) and prepend that directory to the list in $fpath by adding
these commands to your $HOME/.zshrc.

```
fpath=(~/fun $fpath)

autoload -U ~/fun/*(:t)
```

When you next start zsh, the completion system will see the _figlet completion function (and any other completion functions stored in fun).

The leading underscore is necessary because compinit() (the built-in that initializes the completion system) looks in the $fpath directories for files that have leading underscores. Then, based on the first line of the file, compinit() will take various actions.

The first line of any completion function should start with #compdef followed by the command the function performs completions for. That’s the case in Listing One.

Lines 3 and 4 initialize the variables $opt_args, $context, $state, and $line. $opt_args is an associative array that contains command-line options like -d or -f as its keys and the actual parameters to those options (if any) as its values. $state is a scalar variable used by the state mechanism in the _arguments() function (we’ll talk about state later on in this article). The other two variables, $context and $line won’t be used directly in this function, but _arguments() uses them behind the scenes. The reason they are declared as local in this _figlet function is to prevent namespace pollution. (zsh uses dynamic scoping for its variables rather than lexical scoping).

We’ve declared one other variable, $fontdir, on line 5 and it’s unique to the _figlet function. Its purpose is to hold the directory where the figlet fonts are stored. We can find out what this directory is by executing figlet -I2 on line 6, but notice that we don’t just call the program directly.

Instead, we call the _call_program() function to execute figlet -I2 and redirect standard error to /dev/null. To save the output of an external command, you should use _call _program() as it lets advanced users override the actual program that is called. Doing this for figlet is somewhat contrived, but it’s something to be aware of when writing completion functions.

Calling the _arguments Function

With initialization behind us, we’re finally ready to call _arguments(). This function reduces the task of writing completion functions to an exercise in taking human-readable documentation and turning it into something that’s machine-readable. If you run man figlet you’ll see that lines 9 through 34 of the code are identical to the man page.

Let’s start by looking at lines 9 through 12 which describe the options for line justification. Each of these lines has three distinct parts.


An optional exclusion list surrounded by parentheses

The option itself

Help text surrounded by square brackets

The purpose of the exclusion list becomes clear when you ask yourself, “Does it make sense to left-justify and center text at the same time?” It doesn’t, and you have to tell zsh that explicitly. For example, line 12 says that if -r appears on the command line, do not provide the options -x, -l, or -c as completions.

That’s all there is to simple single-letter options. If you look closely, lines 9 through 25 have handled all of figlet‘s single-letter options. Now we have to move on to more complicated types of arguments.

On line 26, we encounter an option that takes an argument. The -w option tells figlet how many characters wide the output should be, and it wants an integer as an argument. There are two things different in line 26. First, the option part has a + after it — that means this option can take another argument in the same word or in the next word. For example, -w 64 and -w72 would both be valid. The second difference is that there’s an additional string after the help text that’s used as a hint when the user presses the tab key (output width (in columns)). However, this text is displayed only if you’ve enabled verbose mode as described in Figure One (pg. 40). The final colon is a separator between the hint and an action, but there’s no action for -w.

On line 29, though, there is an option that does use an action. Here the option (-I) takes an argument. There are only six possible values that can be used with -I; after the hint, we list those six arguments in parentheses.

Line 30 adds another variation. The -d option requires a directory as an argument. We use the zsh internal function called _files() to build and present the list. We pass -/ as an argument so that only directories are presented in the completion list.

Line 31 is the last variation we’ll see in the completion function for _figlet. Sometimes, building a completion list for an option’s arguments is non-trivial and might have to be done with custom code. That’s when we use the state mechanism. See where it says, “->fonts“? That means, “Set the $state variable to ‘fonts’ and let me handle it.”

Up to this point, _arguments() has been doing most of the work, and the flow of control never needed to go past line 34. However, when we use the state mechanism, we’re going to fall through and hit line 36 which will load the $fontdir variable with the value of any -d argument that was specified. Then on line 38, we’re going to hit the case statement.

Based on the value of the $state variable, we’re going to build a completion list for either fonts or control files. In both cases, we’re going to use the _files() function again (lines 40 and 43). We use the -W option to _files() to start the search for completions in the directories that $fontdir contains, and with the -g option, we specify a glob pattern that limits which files are enumerated for the completion list.

And, we’re done. We return 0 if there have been no problems or 1 (or non-zero) if there is any kind of failure (hopefully there won’t ever be). That’s it for a completion function.

What’s Next?

Writing a completion function can be a lot more complicated than our example. If you found the function for figlet too easy, take a look at the completion functions for tar, cvs, and ssh. They can teach you a lot, but your code-reading skills will have to be fairly strong.

Still, you can do a lot with what you’ve learned here. Anything you don’t know can be found out by looking through the documentation. And again, the source of other completion functions is a good place to look for real-life examples (though they will not be well-documented).

Finally, the zsh-workers mailing list (http://zsh.sunsite.dk/Arc/mlist.html) is full of helpful people, so don’t be afraid to subscribe. And remember, if you choose to contribute your work back to the community, realize that you’re helping to making the zsh experience better for everyone.




Reloading Completion Functions


When writing your own completion functions, you’ll find yourself reloading the function frequently for debugging purposes. Here’s a helpful function to blindly unfunction and then autoload everything in ~/fun.



r() {

  local f

  f=(~/fun/*(.))

  unfunction $f:t 2> /dev/null

  autoload -U $f:t

}

