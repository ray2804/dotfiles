request = require 'request'
cheerio = require 'cheerio'
YQL     = require 'yql'
forms   = require("forms")

# Helpers
log = (args...) -> console.log args...

# Data
zipcode     = '60601'
searchTerm  = 'meaning+of+life'
url         = 'http://www.bing.com/search?q=' + searchTerm
tredius     = 'http://www.tredius.nl'
# YQL
qry         = "SELECT * FROM weather.forecast WHERE (location = 60601)"
terms       = "SELECT * FROM search.termextract WHERE context='Italian sculptors and painters of the renaissance favored the Virgin Mary for inspiration' and query='madonna'"

# Forms
fields      = forms.fields
validators  = forms.validators
reg_form    = forms.create(
  username:     fields.string(required: true)
  password:     fields.password(required: true)
  confirm:      fields.password(
    required:       true
    validators:     [validators.matchField("password")]
  )
  email:        fields.email()
)
log reg_form.toHTML()


new YQL.exec terms, (response) ->
    log response.query.results

new YQL.exec qry, (response) ->
    location = response.query.results.channel.location
    condition = response.query.results.channel.item.condition

    log "

     Current weather in
     #{location.city},
     region #{location.region} is #{condition.temp}
     degrees and #{condition.text}

     ".replace(/\s+/g, " ")


request tredius, (err, resp, body) ->
    $       = cheerio.load body
    links   = $('.sb_tlst h3 a')
    $(links).each (i, link) ->
        log $(link).text() + ':\n  ' + $(link).attr('href')



