
Functions
=========

For each element in fpath, the shell looks for three possible files, the newest
of which is used to load the definition for the function: A file created with
the zcompile builtin command, which is expected to contain the definitions for
all functions in the directory named element.

The file is treated in the same manner as a directory containing files for
functions and is searched for the definition of the function.

It may include other function definitions as well, but those are neither loaded
nor executed; a file found in this way is searched only for the definition of
function.

In summary, the order of searching is, first, in the parents of directories in
fpath for the newer of either a compiled directory or a directory in fpath;
second, if more than one of these contains a definition for the function that
is sought, the leftmost in the fpath is chosen; and third, within a directory,
the newer of either a compiled function or an ordinary function definition is
used.

If the `KSH_AUTOLOAD` option is set, or the file contains only a simple
definition of the function, the file’s contents will be executed. This will
normally define the function in question, but may also perform initialization,
which is executed in the context of the function execution, and may therefore
define local parameters.

It is an error if the function is not defined by loading the file. Otherwise,
the function body is taken to be the complete contents of the file. This form
allows the file to be used directly as an executable shell script.

To force the shell to perform initialization and then call the function
defined, the file should contain initialization code in addition to a complete
function definition , and a call to the shell function, including any
arguments, at the end.

It is also possible to create a function that is not marked as autoloaded, but
which loads its own definition by searching fpath, by using ‘autoload -X’
within a shell function.

To load the definition of an autoloaded function myfunc without executing
myfunc, use: 9.2 Anonymous Functions

Arguments to the function may be specified as words following the closing brace
defining the function, hence if there are none no arguments are set. This is a
difference from the way other functions are parsed: normal function definitions
may be followed by certain keywords such as ‘else’ or ‘fi’, which will be
treated as arguments to anonymous functions, so that a newline or semicolon is
needed to force keyword interpretation.

Note that function definitions with arguments that expand to nothing, for
example ‘name=; function $name { ... }’, are not treated as anonymous
functions.

For the functions below, it is possible to define an array that has the same
name as the function with `_functions` appended.

Any element in such an array is taken as the name of a function to execute; it
is executed in the same context and with the same arguments as the basic
function.

Note that if multiple functions are defined using the array `periodic_functions`
only one period is applied to the complete set of functions, and the scheduled
time is not reset if the list of functions is altered.

A hook function may call `fc -p ...` to switch the history context so that the
history is saved in a different file from the that in the global HISTFILE
parameter. This is handled specially: the history context is automatically
restored after the processing of the history line is finished. The following
example function first adds the history line to the normal history with the
newline stripped, which is usually the correct behaviour.

Autoload
========

The base of this autoloading mechanism is the shell parameter $fpath. This
variable contains all the paths where function definition files are located.

Normally functions always have a function definition and body. However, the
files for autoload functions only contain the body.

So to create an autoload function foobar we need to create a file called foobar
inside our autoload directory with the body of our function : # Some autoload
function echo "Hello World"

It is important to know that the function has not yet been executed in any way.
autoload just creates an empty function definition.

The function body will be loaded from the file the first time the function is
called.

If you now rename the source file again, the function will still be able to
run. This autoloading mechanism ensures that the shell stays fast and snappy
even though it provides a lot of features.

Marking functions to be autoloaded with the autoload command only has a very
small memory footprint.

Imagine what would happen if you had an autoload function called myfunction
with the following contents: echo "This is some example file" And you had the
following alias definition in your .zshrc: alias echo="shutdown -h now"
