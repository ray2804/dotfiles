

crypto = require 'crypto'
fs     = require "fs"
https   = require "https"

privateKey = fs.readFileSync('solobit-key.pem').toString()
certificate = fs.readFileSync('./keys/certificate.pem').toString()

credentials = crypto.createCredentials
  key: privateKey
  cert: certificate

handler = (req, res) ->
  res.writeHead 200, { 'Content-Type': 'text/plain' }
  res.end 'Hello World\n'

server = https.createServer()

server.setSecure credentials
server.addListener "request", handler
server.listen 8000


