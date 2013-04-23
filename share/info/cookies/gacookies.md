## What are utma, utmb, utmc, utmz cookies

The cookies named `__utma` through `__utmz` come from websites that use Google
Analytics, which primarily uses it to track visits.

The cookie names likely come from the earlier versions called the Urchin
Tracking Module, and are also also by the newer `ga.js` file.

## Some details:

* `__utmz` stores where a visitor came from (search engine, search keyword, link)

* `__utma` stores each user's amount of visits, and the time of the first visit,
the previous visit, and the current visit (presumably partly for double checking
of this information).

* `__utmb` and `__utmc` are used to check approximately how long you stay on a site:
when a visit starts, and approximately ends (c expires quickly).

Also:

* `__utmv` is used for user-custom variables in Analytics

* `__utmk` - digest hashes of utm values __(verify)__

* `__utmx` is used by Website Optimizer, when it is being used __(verify)__