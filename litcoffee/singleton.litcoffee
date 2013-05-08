
# Singleton Pattern

## Problem

[Many times || cyclical[]:pattern [in workflow] you only want one (1), and
*only* one, [particular][:identity] `(specimen produced from the
blueprint)`[instance][of a class].

For example, you may only need one class that creates server resources and you
want to ensure that the one object can control those resources. Beware,
however, because the [singleton pattern][p:Singleton] can be easily abused to
mimic unwanted global variables.

## Helpers

    log = console.log

## Solution

The publicly available class only contains the method to get the one true
instance. The instance is kept within the closure of that public object and is
always returned.

This is works because CoffeeScript allows you to define executable statements
inside a class definition. However, because most CoffeeScript compiles into a
IIFE wrapper you do not have to place the private class inside the class
definition if this style suits you. The later might be useful when developing
modular code such as found in CommonJS (Node.js) or Require.js (See the
discussion for an example).

    class SingletonA

You can add statements inside the class definition which helps establish
private scope (due to closures) instance is defined as null to force correct
scope.

        instance = null

Create a [private scope][private_scope] [nested class][nested_class] that we
can [initialize][initialize] but [define][declaration] in [this][c:SingletonA]
[scope][class_scope] to [force][] the use of the singleton class.

        class PrivateClass

            constructor: (@message) ->

            echo: -> @message

This is a static method used to either retrieve the instance or create a new
one `?=`.

        @get: (message) -> instance ?= new PrivateClass(message)

    a = Singleton.get "Hello A"
    a.echo()

> Result => "Hello A"

Now we try to access the single

    b = Singleton.get "Hello B"
    b.echo()

> Result => "Hello A"

    Singleton.instance # => undefined

    a.instance # => undefined

    Singleton.PrivateClass # => undefined

### Discussion

See in the above example how all instances are outputting from the same
instance of the Singleton class. You can also see that the PrivateClass and
instance variable are not accessible outside the Singleton class. In essance
the Singleton class provides a static method get which returns only one
instance of PrivateClass and only one. It also hides the PrivateClass from the
world so that you can not create your own.

The idea of hiding or making private the inner workings is preference.
Especially since by default CoffeeScript wraps the compiled code inside it’s
own IIFE (closure) allowing you to define classes without worry that it might
be accessible from outside the file. In this example, note that I am using the
idiomatic module export feature to emphasize the publicly accessible portion of
the module. (See this discussion for further explanation on exporting to the
global namespace).

    root = exports ? this

Create a private class that we can initialize however defined inside the
wrapper scope.

    class ProtectedClass

        constructor: (@message) ->

        echo: -> @message

You can add statements inside the class definition which helps establish
private scope (due to closures) instance is defined as null to force correct
scope.

    class SingletonB

This is a static method used to either retrieve the instance or create a new
one.

        instance = null

        @get: (message) -> instance ?= new ProtectedClass(message)

# Export Singleton as a module

        root.Singleton = Singleton

Note how incredibly simple coffeescript makes this design pattern. For
reference and discussion on nice javascript implementations, check out
Essential JavaScript Design Patterns For Beginners.


A class/ prototype construction that holds some local wrapped instances of
environment objects, be it node.js or browser client-side JavaScript

    class Superstructuur

        constructor: (@global   =   global
                    , @root     =   root
                    , @process  =   process
                    , @log      =   console.log
        ) ->

        isaWindow: -> false if not window?
        i18Locale: -> @isaWindow ? "Is a window from the browser" : "Is not a window so Node.js"

        sayHello: (target) ->
            alert "Hello, #{target}!"

    unless root.tester
        root.tester = new Superstructuur()




