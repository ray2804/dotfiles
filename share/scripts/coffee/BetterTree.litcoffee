There's basically two ways of accomplishing this. In an async
environment you'll notice that there are two kinds of loops: serial and
parallel.


In a parallel loop, all the iterations are
started at the same time, and one may complete before another, however,
it is much faster than a serial loop. So in this case, it's probably
better to use a parallel loop because it doesn't matter what order the
walk completes in, just as long as it completes and returns the results
(unless you want them in order).

A parallel loop would look like this:


    fs = require("fs")

    parallel = (dir, done) ->
        results = []

        fs.readdir dir, (err, list) ->
            return done(err)  if err
            pending = list.length
            return done(null, results)  unless pending

            list.forEach (file) ->
                file = dir + "/" + file

                fs.stat file, (err, stat) ->
                    if stat and stat.isDirectory()
                        parallel file, (err, res) ->
                            results = results.concat(res)
                            done null, results  unless --pending

                    else
                        results.push file
                        done null, results  unless --pending


A serial loop waits for one iteration to complete before it
moves onto the next iteration - this guarantees that every iteration of
the loop completes in order. 

A serial loop would look like this:

    serial = (dir, done) ->
        results = []

        fs.readdir dir, (err, list) ->

            return done(err) if err

Lists are of varying length, per directory we have a list

Since it's pretty lightweight to assign an anonymous function to a local
variable, the idiom below might be more preferred.

            i = 0
            next = (->
                file = list[i++]
                return done(null, results)  unless file
                file = dir + "/" + file

                fs.stat file, (err, stat) ->

If this is a directory, call the top function with our location

                    if stat and stat.isDirectory()
                        serial file, (err, res) ->
                            results = results.concat(res)
                            next arguments.callee

Add to the array and call own name

                    else
                        results.push file
                        next arguments.callee
            )

            


And to   test it out on your home directory (WARNING: the results list
will be huge if you have a lot of stuff in your home directory):

    serial __dirname, (err, results) ->
        throw err if err
        console.log(results)


