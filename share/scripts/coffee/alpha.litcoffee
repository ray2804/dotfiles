#### Credits
I found these examples scattered throughout the web and included the original links where *I* found them. The authors or persons who published them on these sites are referenced over there so follow the links for them or message me if you want your name explicit here.

## Cheat sheets

* <http://coffeescriptcookbook.com/chapters>
* <http://www.cheatography.com/dimitrios/cheat-sheets/coffeescript-cheat-sheet/>

## Helpers

These are helper functions as this document is also a `.litcoffee` literate CoffeeScript executable script.

``` coffeescript

log = (args...) -> console.log args...

```

## Loops

### List comprehensions

``` coffeescript

    books = [
      {name: 'Dune', rating: 5}
      {name: "Old Man's War", rating: 4}
      {name: 'Foundation', rating: 3}
    ]
    good = (b.name for b in books when b.rating > 3)

```

### `for..in` Iterate members of **array** collection
<http://openmymind.net/2012/1/15/Understanding-CoffeeScript-Comprehensions/>

In CoffeeScript, if you want to loop through an array, you use `for in`. To loop through hashes/objects you use `for of`. For example:

``` coffeescript

    heroes = ['leto', 'duncan', 'goku']

    # boring
    for hero, index in heroes
      if index % 2 == 0
        log(hero)

    # Comprehensions allows for conditions via when, so we do:
    console.log hero for hero, index in heroes when index % 2 == 0

    likes =
      leto: 'spice'
      paul: 'chani'
      duncan: 'murbella'

    log('%s likes %s', key, value) for key, value of likes

```

Or, including the index

``` coffeescript

    for hero, index in heroes
      log('The hero at index %d is %s', index, hero)

```

There are other things you can do with compressions. For example, you won't always want to execute some code (like console.log) on each item. Instead you might want to select or map the results into another variable:

``` coffeescript

    evenHeroes = (hero for hero, index in heroes when index % 2 == 0)

```

The parenthesis in the above code are critical. Without them, only the last value of our comprehension ('goku') will be assigned. With them, the filtered array is assigned. The very first hero in the above code is what's being selected from our compression.

### Pythagorean triples
<http://javascript.cs.lmu.edu/notes/coffeescript/>

This script prints some Pythagorean triples.

``` coffeescript

    for c in [1..100]
      for b in [1..c]
        for a in [1..b]
          if a * a + b * b == c * c
            log "(#{a},#{b},#{c})"

```

### `for..of` Iterate a **Object** or hash-map

``` coffeescript

    likes =
      leto: 'spice'
      paul: 'chani'
      duncan: 'murbella'

    for key of likes
      log(key)

```

Or, including the value

``` coffeescript

  for key, value of likes
    log('%s likes %s', key, value)

```

### Array of objects
<http://coffeescriptcookbook.com/chapters/arrays/list-comprehensions>

You have an array of objects and want to map them to another array, similar to Python’s list comprehensions.

``` coffeescript

electric_mayhem = [ { name: "Doctor Teeth", instrument: "piano" },
                    { name: "Janice", instrument: "lead guitar" },
                    { name: "Sgt. Floyd Pepper", instrument: "bass" },
                    { name: "Zoot", instrument: "sax" },
                    { name: "Lips", instrument: "trumpet" },
                    { name: "Animal", instrument: "drums" } ]

names = (muppet.name for muppet in electric_mayhem)
# => [ 'Doctor Teeth', 'Janice', 'Sgt. Floyd Pepper', 'Zoot', 'Lips', 'Animal' ]

```

Alternatively, you may use map() with an anonymous function

``` coffeescript

    names = electric_mayhem.map (muppet) -> muppet.name

```

Invert the inner loop and put the loop construct after the expression:
<http://stackoverflow.com/questions/11182414/inline-iteration-key-value-of-object-in-array>

``` coffeescript

    for row in rows
      log "#{k}: #{v}" for k,v of row

```

And, as that inner loop is also an expression, you can also invert the outer loop the same way :D

``` coffeescript

    log "#{k}: #{v}" for k,v of row for row in rows

```

The most similar to what you were trying to write is probably this:

``` coffeescript

    for row in rows then for k,v of row
      log "#{k}: #{v}"
```

Which can be further in-lined using another then (the then keyword is usually equivalent to a newline and adding one level of indentation):

``` coffeescript

    for row in rows then for k,v of row then alert "#{k}: #{v}"

```

All of these alternatives generate the same JavaScript code, so picking one or another will not result on degraded performance or anything like that.

## Statement modifiers

Statement modifiers are a neat and useful way in CoffeeScript and in Ruby to execute an `if` (or `unless`) statement.

``` coffeescript

   # instead of
   if x == 0
     x = 1

   # we ca do
   x = 1 if x == 0

```

## Symbols and operators
<http://openmymind.net/2012/5/16/Ten-Features-I-Like-About-CoffeeScript/>

### `=>` Fat arrow
<http://openmymind.net/2012/5/16/Ten-Features-I-Like-About-CoffeeScript/>

CoffeeScript lets you define a method with a fat arrow `=>`. Using `=>` binds the function to the expected scope, passing along the correct context from outer scope inwards:

``` coffeescript

    class DataTable
      constructor: (element) ->
        this.element = $(element).on 'click', 'tr', this.clicked

      clicked: (e) =>
        this.highlight()

      highlight: =>
        ....

```

### `@` Alias (this)
<http://openmymind.net/2012/5/16/Ten-Features-I-Like-About-CoffeeScript/>

Speaking of `this`, CoffeeScript has a shortcut for it, the `@` symbol. It's easy to write this off as meaningless syntactical sugar, but it is useful. First, constructor parameters prefixed with `@` are converted into properties:


``` coffeescript

    class User
      constructor: (@id) ->

```

Neither `@` nor the fat arrow mean that you don't have to worry about the current meaning of this (or its alias @). They aren't silver bullets, which isn't to say that they don't add value. For example, `@` is also a nice way to define class method:

``` coffeescript

    class User
      constructor: (@id) ->

      @findById: (id) =>
        # ...

```

### `...?` Existential operator

In CoffeeScript, `==` and `!=` translate to `===` and `!==`, which you probably always want to be using. This alone removes a common pain point. In addition to that, there's the existential operator. In its simplest form, it checks for undefined and null:

``` coffeescript

    return null unless name?

```

But it can also be used in chains:

``` coffeescript

    class Unicorn
      is_rookie: () ->
        vampire?.kills > 0

```



### Named and optional arguments
<http://stackoverflow.com/questions/5238398/default-function-parameter-ordering>

Demonstrates an idiomatic way of implementing named arguments in CoffeeScript. A compromise between cleanness of definition and of calling using pattern matching so that you don't need to use another identifier (opts).

``` coffeescript

    fill = ({container, liquid}) ->
      container ?= "mug"
      liquid ?= "coffee"

      "Filling the #{container} with #{liquid}..."

      console.log fill liquid: "juice", container: "glass"

```

This also work nicely if you want positional arguments followed by keyword arguments:

``` coffeescript

    fill = (quantity, {container, liquid}) ->
      quantity ?= "500 mL"
      container ?= "mug"
      liquid ?= "coffee"

      "Filling the #{container} with #{quantity} of #{liquid}..."

      console.log fill "1L", liquid: "juice", container: "glass"
      # becomes
      console.log(fill("1L", { liquid: "juice", container: "glass" }))

```

Or if you have many arguments and you want to name them and have default values, you could do something like this:

``` coffeescript

    fill = (opts = {}) ->
      opts.container ?= "mug"
      opts.liquid ?= "coffee"
      "Filling the #{opts.container} with #{opts.liquid}..."

    console.log fill
      liquid:"juice"
      container:"cup"

    console.log fill
      liquid:"juice"

    console.log fill()

```


## Functions


### Plain old functions

``` coffeescript

    isOdd = (x) -> x % 2 == 1
    twoXPlusY = (x, y) -> 2 * x + y
    square = (x) -> x * x
    addSix = (x) -> x + 6

```

The value of a variable can be a function

``` coffeescript

    addSixThenSquare = compose square, addSix
    addTwelve = twice addSix

    console.log addSixThenSquare(9)    # 225
    console.log twice(square)(3)       # 81
    console.log addTwelve(100)         # 112

```
### Higher order functions
<http://javascript.cs.lmu.edu/notes/coffeescript/>
<http://en.wikipedia.org/wiki/Higher-order_function>

Examples of higher order functions in CoffeeScript. This script contains both examples of defining your own higher order functions, and how you use the built-ins map, reduce, and filter.

Returns the functional composition of `f` and `g`. Second returns the function which applies f to its argument twice

``` coffeescript

    compose = (f, g) ->
      (x) -> f g x

    twice = (f) ->
      compose f, f

```

Some examples found on <https://news.ycombinator.com/item?id=4927399> which demonstrate how far one can take this.

``` coffeescript

    partial = (fn, a...) -> (b...) -> fn.apply this, [a..., b...]
    compose = (fns...) -> (a) -> a = fn a for fn in fns; a

    splat = (fn) -> (arr) -> arr.map fn
    get = (attr) -> (obj) -> obj[attr]

    maybe = (fn) -> (args...) ->
      return unless args.length
      return arg for arg in args when not arg?
      fn.apply this, args

```

Higher-order function takes a function to morph behavior

``` coffeescript

    console.log = (msg)-> $('body').append "<p>#{msg}</p>"

    count = (items, matching)->
      (item for item in items when matching item).length

```

Stick some useful collaborating functions right on the higher-order function

``` coffeescript

    count.eql = (target)->
      (item)-> item == target

    count.lessThan = (target)->
      (item)-> item < target

    count.greaterThan = (target)->
      (item)-> item > target

    #usage
    samples = [1,2,3,4,5,1,2,3,5,3,1,2,5,6]
    console.log 'Equal: ' + count samples, count.eql 5
    console.log 'Greater: ' + count samples, count.greaterThan 4

```

### Maps, filter and reduce

<http://ricardo.cc/2011/06/02/10-CoffeeScript-One-Liners-to-Impress-Your-Friends.html>

Map, filter, and reduce. map and filter are not used too much; comprehensions are
generally more appropriate.

``` coffeescript

    console.log [0..4].map square                       # [0, 1, 4, 9, 16]
    console.log [0..6].filter isOdd                     # [1, 3, 5]
    console.log [3, 6, -4, 2, -7].reduce(twoXPlusY, 0)  # 77

```

But we can do exactly the same using as map, first using a range literal and an anonymous function. Last, Javascript (and CoffeeScript by extension) also has native map and reduce functions:

``` coffeescript

    [1..10].map (i) -> i*2

    # we can also write the more expressive form
    i * 2 for i in [1..10]

    [1..1000].reduce (t, s) -> t + s
    # (reduce == reduceLeft, reduceRight is also available)

```

First, a 1 to 1 mapping to the Scala version with a bit of string interpolation thrown in the mix:

``` coffeescript

    [1..4].map (i) -> console.log "Happy Birthday " + (if i is 3 then "dear Robert" else "to You")

```

But it get's better. This reads almost like pseudo-code:

``` coffeescript

    console.log "Happy Birthday #{if i is 3 then "dear Robert" else "to You"}" for i in [1..4]

```

Filter a list of numbers into two categories. The literate way:

``` coffeescript

    (if score > 60 then (passed or passed = []) else (failed or failed = [])).push score for score in [49, 58, 76, 82, 88, 90]

```

And the functional way:

``` coffeescript

    [passed, failed] = [49, 58, 76, 82, 88, 90].reduce ((p,c,i) -> p[+(c < 60)].push c; p), [[],[]]

```

### String operations

<http://ricardo.cc/2011/06/02/10-CoffeeScript-One-Liners-to-Impress-Your-Friends.html>

Verify if word exists in string: Too easy since we have the some method. It returns true if any of the elements in the array satisfies the function:

``` coffeescript

    wordList = ["coffeescript", "eko", "play framework", "and stuff", "falsy"]
    tweet = "This is an example tweet talking about javascript and stuff."

    wordList.some (word) -> ~tweet.indexOf word

```

This will return the matched words instead. `~` is not a special operator in CoffeeScript, just a dirty trick. It is the bitwise `NOT` operator, which inverts the bits of it's operand. In practice it equates to `-x-1`. Here it works on the basis that we want to check for an index greater than `-1`, and `-(-1)-1 == 0` evaluates to false.

``` coffeescript

    wordList.filter (word) -> ~tweet.indexOf word


```

### File operations (and AJAX)

Users of client-side javascript frameworks will be familiar with this idea of reading files. First the **asynchronous** version, after that, second the **synchronous** one. In node.js land this (synchronous functions) is only acceptable for application start-up routines. You should use the async version in your code. Third and last: fetch and Parse a JSON.

``` coffeescript

    fs.readFile 'data.txt', (err, data) -> fileText = data

    fileText = fs.readFileSync('data.txt').toString()

    request.get { uri:'path/to/api.json', json: true }, (err, r, body) -> results = body

```

The apply function comes handy here. It allows you to call a function passing an array as the list of arguments: Math.max and Math.min both receive a variable number of arguments, i.e. Math.max 30, 10, 20 returns 30. Let's put it to work with an array:

``` coffeescript

    Math.max.apply @, [14, 35, -7, 46, 98] # 98
    Math.min.apply @, [14, 35, -7, 46, 98] # -7

```

### Sieve of Eratosthenes
<http://ricardo.cc/2011/06/02/10-CoffeeScript-One-Liners-to-Impress-Your-Friends.html>

The only low-level loop that CoffeeScript provides is the `while` loop. If possible, you want to use comprehensions.

``` coffeescript

    sieve = (num) ->
      numbers = [2..num]
      while ((pos = numbers[0]) * pos) <= num
        delete numbers[i] for n, i in numbers by pos
        numbers.shift()
      numbers.indexOf(num) > -1

```

Compact version:

``` coffeescript

    primes = []
    primes.push i for i in [2..100] when not (j for j in primes when i % j == 0).length

```

Which we can then use for a real one-liner like the original:

``` coffeescript

    (n) -> (p.push i for i in [2..n] when not (j for j in (p or p=[]) when i%j == 0)[0]) and n in p

```

Or the somewhat more efficient

``` coffeescript

    (n) -> (p.push i for i in [2..n] when !(p or p=[]).some((j) -> i%j is 0)) and n in p

```

## Bonus
Most readable fizzbuzz version you'll ever see:

``` coffeescript

    "#{if i%3 is 0 then 'fizz' else ''}#{if i%5 is 0 then 'buzz' else ''}" or i for i in [1..100]
```

Even simpler, but trickier, with a little hint by satyr:

``` coffeescript

    ['fizz' unless i%3] + ['buzz' unless i%5] or i for i in [1..100]

```

When you use the `+` operator on an Array, *it converts it to a **string** *. `[].toString()` is the same as `[].join(',')`, which gives an empty string in case the array value is `undefined` or `null`. This also works in Javascript (`[undefined] + "b" === "b"`).


## Command liners
<http://javascript.cs.lmu.edu/notes/coffeescript/>

### Current process

This script writes successive prefixes of its first command line argument to standard output.  It assumes that coffeescript is running under node.js, so the script would be invoked as `$ coffee prefixes.coffee thestringtogenerateprefixesof`
so the actual argument shows up at `process.argv[2]`.

``` coffeescript

    s = process.argv[2];
    console.log s.slice(0, i) for i in [0..s.length] if s

```

## Shortcuts

### Limited (break-early or short-circuiting) loop
<http://stackoverflow.com/questions/10920186/how-can-i-get-the-key-and-value-from-an-object-without-a-for-loop>

If you only want to check if an object has exactly one key then you could use a short circuiting loop. The basic idea is to use a `for ... of` loop to start counting the number of keys in the object. There are only three cases we care about. (1) fewer than one key (i.e. null or no keys). (2) There is exactly one key. (3) There are more than one keys. In this case the loop will only make two iterations due to the break. That will function will make at most two iterations of the loop.

``` coffeescript

    exactly_one = (obj) ->
      return false if(!obj)
      how_many = 0
      for own key of obj
        break if(++how_many == 2)
      how_many == 1

```
Minor adjustments to the logic would give you "at most one", "at least one", etc. If you weren't born a C programmer, then you could unroll the logic a little bit:

``` coffeescript

    for own key of obj
      ++how_many
      break if(how_many == 2)

```
