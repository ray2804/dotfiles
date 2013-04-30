

Node Progress
=============
[Original work by visionmedia](https://github.com/visionmedia/node-progress)
Flexible ascii progress bar

Installation
------------

npm install progress

```coffee
    ProgressBar = require("progress")
    bar = new ProgressBar(":bar",
      total: 20
    )
    timer = setInterval(->
      bar.tick()
      if bar.complete
        console.log "\ncomplete\n"
        clearInterval timer
    , 100)


