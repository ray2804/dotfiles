
Source: <http://elegantcode.com/2011/06/30/exploring-coffeescript-part-2-variables-and-functions/>

CoffeeScript is a neat little language that compiles down to JavaScript code.
Its syntax is heavily inspired by Ruby and next to bringing a lot of nice
language features to the table, it also requires less amount of code than
writing the equivalent directly in JavaScript.

## Variables

Declaring a [variable][variable] in CoffeeScript is done like this, variable `text`
actually `var text;` in JavaScript, gets a string of "some string" set as its
value.

    text = "some string"

Because `var` is a [reserved][reserved] [keyword][keyword] we can't use that
anywhere really. Consider some notation/style play in the following examples
that show assignment of other data types to these variables:

    vara = "another string of text"
    var2 = 1++
    var3 = 3 % 9
    varA = vara.toUppercase()
    vArr = ['hello', "world", 'you can do like', 1, 2, 3, { en: 'Voila!' } ]

But enough about data types, thats for another part.

    interpolation   = "string injection"
    quoteSingle     = 'The literal string doesn\'t have #{interpolation} support'
    quoteDouble     = "The trick of #{interpolation} uses in fact double quotes"

There’s no need to use the `var` keyword like in JavaScript. In fact, using the
var keyword results in a compiler error. We also don’t need to provide a
semi-colon at the end of the statement. These are added automatically for you
during compilation. When we omit semi-colons in JavaScript, the engine also
tries to add them at runtime but this behavior can cause a [lot of grief][grief].
CoffeeScript correctly adds them for us, and although we no longer need to type
in semi-colons, we’re still able to do so if we want to.

The variable declaration from above translates into the following JavaScript:

```js
(function() {
  var podcast;
  podcast = 'Astronomy cast';
}).call(this);
```

The resulting code here is actually quite interesting. When we want to declare
a local variable in JavaScript and we forget to specify the var keyword, then
this variable ends up becoming a global variable instead.

```js
/\*var\*/ f  = 'fubar';
console.log(window.f);    // Outputs 'fubar'
```

This can no longer happen just by accident when using CoffeeScript as it wraps
all generated JavaScript in a self-executing function and by automatically
emitting the var keyword before the variable name. We’re still able to create
global variables but the only way is to declare them explicitly.

window.f = 'fubar'

> Note that the wrapper can be disabled since a few releases, using the `-b`
swittch.

The use of global variables is strongly discouraged when using JavaScript. As a
result, the same goes for CoffeeScript as well.

## Functions

The tedious function keyword in JavaScript is not part of the CoffeeScript
syntax. Instead it uses an arrow `->` to define a function.

    log = (message) -> console.log message
    log 'This example shows the simple fact that parentheses are no longer here'

    log("We can also switch quotes, #{interpolate} and make the method argument encapsulation explicit parens `(` and `)` explicit")


[grief]: <http://elegantcode.com/2011/01/12/basic-javascript-part-6-automatic-semicolon-insertion/>


