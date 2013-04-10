
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
