/**
 * Allow language sidebar links to oldwikisource
 * by [[user:Kingpin13]] and [[User:Doug]]
 * based on the interwikiExtra script by [[user:ThomasV]]
 */
function interwikiOldws() {
	// iterate over all <span>-elements
	for(var i=0; a=document.getElementsByTagName('span')[i]; i++) {
		// if found a linkInfo span
		if(a.className == 'interwiki-oldws') {
			var match = /(\d+)$/.exec(a.id);
			var skipped = parseInt(match[1]) - 1;
 
			// iterate over all <li>-elements
			for(var j=0; b=document.getElementsByTagName('li')[j]; j++) {
				if (b.className == 'interwiki-' + a.id.replace(/\d+$/, "")) {
					if (skipped > 0)
						skipped = skipped - 1;
					else
					{
				        	b.innerHTML = '<a href="http://wikisource.org/wiki/'+mw.html.escape(a.title)+'">'+'Multilingual'+'</a>';
                                        	break;
					}
				}
			}
		}
	}
}
addOnloadHook(interwikiOldws);