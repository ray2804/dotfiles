/**********************
*** add menu on edit page /for selecting subsets of special characters
*** by [[user:Pathoschild]]
***   - note: must match MediaWiki:Edittools
**********************/
function addCharSubsetMenu() {
	var specialchars = document.getElementById('specialchars');
 
	if (specialchars) {
		var menu = "<select style=\"display:inline\" onChange=\"chooseCharSubset(selectedIndex)\">";
		menu += "<option>Select</option>";
		menu += "<option>Ligatures and symbols</option>";
		menu += "<option>Accents</option>";
		menu += "<option>Tildes</option>";
		menu += "<option>Cedillas</option>";
		menu += "<option>Diereses</option>";
		menu += "<option>Circumflexes</option>";
		menu += "<option>Macrons</option>";
		menu += "<option>Other diacritics</option>";
		menu += "<option>Greek</option>";
		menu += "<option>Hebrew</option>";
		menu += "<option>Cyrillic</option>";
		menu += "<option>IPA</option>";
		menu += "</select>";
		specialchars.innerHTML = menu + specialchars.innerHTML.replace(/_newline_/gm, "\n");
 
		/* default subset - try to use a cookie some day */
		chooseCharSubset(0);
	}
}
 
/* select subsection of special characters */
function chooseCharSubset(s) {
	var l = document.getElementById('specialchars').getElementsByTagName('p');
	for (var i = 0; i < l.length ; i++) {
		l[i].style.display = i == s ? 'inline' : 'none';
		l[i].style.visibility = i == s ? 'visible' : 'hidden';
	}
}
 
addOnloadHook(addCharSubsetMenu); //remove this if the edittools loads separately