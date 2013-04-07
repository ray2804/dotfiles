Reader forms
============

Symbols
-------

Symbols begin with a non-numeric character and can contain alphanumeric 
characters and `*`, `+`, `!`, `-`, `_`, and `?` (other characters will 
be allowed eventually, but not all macro characters have been determined). 

* `/` has special meaning, it can be used once in the middle of a symbol
to separate the namespace from the name, e.g. `my-namespace/foo`. `/` by 
itself names the division function. 

* `.` has special meaning - it can be used one or more times in the middle of 
a symbol to designate a fully-qualified class name, e.g. `java.util.BitSet`, 
or in namespace names. 

* Symbols beginning or ending with `.` are *reserved* by Clojure.

* Symbols containing `/` or `.` are said to be **qualified**. 

* Symbols beginning or ending with `:` are *reserved* by Clojure. 

* A symbol *can* contain one or more non-repeating `:`s.

Literals
--------

* <u>Strings</u> - Enclosed in **"double quotes"**. May span multiple lines. 
Standard Java escape characters are supported.

* <u>Numbers</u> - as per Java, plus indefinitely long integers are supported, 
as well as ratios, e.g. `22/7`. Floating point numbers with an `M suffix` are 
read as `BigDecimals`. Integers can be specified in any base supported by 
`Integer.parseInt()`, that is any radix from 2 to 36; for example 2r101010, 
8r52, 36r16, and 42 are all the same Integer.

* <u>Characters</u> - preceded by a backslash like `\c`. `\newline`, `\space` 
and `\tab` yield the corresponding characters.

* <u>nil</u> Means *nothing/no-value* - represents Java null and tests logical
false

* <u>Booleans</u> - true and false

### Keywords 

Keywords are like symbols, except:
* They can and must begin with a colon, e.g. `:fred`.
* They cannot contain `.` or name classes.
* A keyword that begins with two colons is resolved in the current namespace:
In the user namespace, `::rect` is read as `:user/rec`

Lists
-----

* Lists are zero or more forms enclosed in parentheses:
`(a b c)`

Vectors
-------

* Vectors are zero or more forms enclosed in square brackets:
`[1 2 3]`

Maps
----

* Maps are zero or more key/value pairs enclosed in braces:
`{:a 1 :b 2}`
* Commas are considered whitespace, and can be used to organize the pairs:
`{:a 1, :b 2}`
* Keys and values can be any forms.

Sets
----

* Sets are zero or more forms enclosed in braces preceded by #:
`#{:a :b :c}`

Deftype, defrecord
------------------
(>=1.3)

* `Deftype`, `defrecord` and `any` constructor calls:
Calls to Java class, deftype, and defrecord constructors can be called using 
their fully qualified class name preceded by # and followed by a vector:
`#my.klass_or_type_or_record [:a :b :c]`

* The elements in the vector part are passed unevaluated to the relevant 
constructor. `defrecord` instances can also be created with a similar form 
that takes a map instead: `#my.record {:a 1, :b 2}`

* The keyed values in the map are assigned unevaluated to the relevant fields 
in the `defrecord`. Any defrecord fields without corresponding entries in the 
literal map are assigned nil as their value. Any extra keyed values in the map
literal are added to the resulting defrecord instance.


Macro characters
================

The behavior of the reader is driven by a combination of built-in constructs and 
an *extension system* called the **read table**. Entries in the read table provide 
mappings from certain characters, called **macro characters**, to specific reading 
behavior, called **reader macros**. Unless indicated otherwise, macro characters 
cannot be used in user symbols.

* Quote `'` works as in `'form => (quote form)`
* Character `\ ` (backslash) as per above, yields a character literal.
* Comment `;` Single-line comment, causes the reader to ignore everything from the 
semicolon to the end-of-line.
* Deref `@` (at) works as in `@form => (deref form)`
* Metadata `^` (above)
* *Symbols*, *Lists*, *Vector*, *Sets* and *Maps* can have metadata, which is a map 
associated with the object. The metadata reader macro first reads the metadata and 
attaches it to the next form read:

> `^{:a 1 :b 2} [1 2 3]` yields the vector `[1 2 3]` 
> with a metadata map of `{:a 1 :b 2}`

A shorthand version allows the metadata to be a simple symbol or keyword, in which 
case it is treated as a single entry map with a key of :tag and a value of the symbol 
provided, e.g.:

> `^String x` is the same as `^{:tag String} x`

Such tags can be used to convey type information to the compiler.

* Dispatch `#` - The dispatch macro causes the reader to use a reader macro from another 
table, indexed by the character following #: `#{}` - see *Sets* above

* Regex patterns `#"pattern"` - A regex pattern is read and compiled at read time. The 
resulting object is of type `java.util.regex.Pattern`.

* Var-quote `#'` works as in `#'x => (var x)`

* Anonymous function literal `#()` works as in `#(...) => (fn [args] (...))` - where args 
are determined by the presence of argument literals taking the form `%`, `%n` or `%&`. 
`%` is a synonym for `%1`, `%n` designates the nth arg (1-based), and `%&` designates a 
rest arg. This is not a replacement for fn - idiomatic used would be for very short one-off 
mapping/filter fns and the like. `#()` forms cannot be nested.

* Ignore next form `#_` - The form following `#_` is completely skipped by the reader. 
(This is a more complete removal than the comment macro which yields nil).

* Syntax-quote (`\``, note, the "backquote" character), Unquote `~` and Unquote-splicing `~@`
For all forms other than Symbols, Lists, Vectors, Sets and Maps, ```x` is the same as `'x`.

* For Symbols, syntax-quote resolves the symbol in the current context, yielding a 
fully-qualified symbol (i.e. namespace/name or fully.qualified.Classname). 

If a symbol is non-namespace-qualified and ends with `#`, it is resolved to a generated symbol 
with the same name to which `'_'` and a unique id have been appended. e.g. `x#` will resolve 
to `x\_123`. All references to that symbol within a syntax-quoted expression resolve to the same 
generated symbol.

For *Lists/Vectors/Sets/Maps*, syntax-quote establishes a template of the corresponding data 
structure. Within the template, unqualified forms behave as if recursively syntax-quoted, but 
forms can be exempted from such recursive quoting by qualifying them with unquote or
unquote-splicing, in which case they will be treated as expressions and be replaced in the 
template by their value, or sequence of values, respectively.