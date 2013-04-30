
ansiColorizer = require("ansi-colorizer")

colorizer = ansiColorizer.configure({ color: true, bright: false })
colorizer.red("Hey")

colorizer = ansiColorizer.configure({ color: true, bright: true })
colorizer.red("Hey")

colorizer = ansiColorizer.configure()
colorizer.red("Hey")


