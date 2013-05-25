function disable_input(set)
{
    if (set) {
        $(document).keyup(function(e) {
            if (e.which == 27) { disable_input(false); }
        });
    }

    var input = document.getElementById("wsOcr1");
    if (input) input.onclick = set ? null : add_ocr;

    var input = document.getElementById("wsOcr2");
    if (input) input.onclick == set ? null : fraktur_ocr;

   var tb = document.getElementById("wpTextbox1");
   tb.disabled = set;
}

function ocr_callback(data) {
   var tb = document.getElementById("wpTextbox1");
   if (data['error']) {
       alert(data['text']);
   } else {
        // Checking if tb is disabled is required with chrome as ESC doesn't kill
        // the query.
       if (tb.disabled)
           tb.value = data['text'];
   }

   disable_input(false);
}

/**
 * Fetches a url of the image thumbnail.
 * Note: the callback will not be called if the attempt was unsuccessful.
 */
function ocr_fetch_thumb_url( requestedWidth, callback ) {
	var fullWidth = mw.config.get( 'proofreadPageWidth' );
	var fullHeight = mw.config.get( 'proofreadPageHeight' );

	// enforce quantization: width must be multiple of 100px
	var quantizedWidth = 100 * Math.round( requestedWidth / 100 );

	// compare to the width of the image
	if ( quantizedWidth < fullWidth ) {
		var request = {
			action: 'query',
			titles: mw.config.get( 'proofreadPageFileName' ),
			prop: 'imageinfo',
			iiprop: 'url|size',
			iiurlwidth: quantizedWidth,
			format: 'json'
		};

		// Check if this is multipaged document
		if ( mw.config.get( 'proofreadPageFilePage' ) != null ) {
			request['iiurlparam'] = 'page' + mw.config.get( 'proofreadPageFilePage' ) + '-' + quantizedWidth + 'px';
		}

		// Send request to fetch a thumbnail url
		jQuery.getJSON( mw.util.wikiScript( 'api' ), request, function( data ) {
			if ( data && data.query && data.query.pages ) {
				for ( var i in data.query.pages ) {
					var page = data.query.pages[i];
					if ( !page.imageinfo || page.imageinfo.length < 1 ) {
						continue;
					}
					var imageinfo = page.imageinfo[0];

					if ( imageinfo.thumburl ) {
						callback( imageinfo.thumburl, imageinfo.thumbwidth, imageinfo.thumbheight );
					}

					return;
				}
			}
		} );
	} else {
		// Image without scaling
		callback( 'https:' + mw.config.get( 'proofreadPageURL' ), fullWidth, fullHeight );
	}
}

function do_ocr(url) {
    disable_input(true);
 
    var request_url = "https://toolserver.org/~phe/ocr.php?url="+url+"&lang="+wgContentLanguage+"&user="+wgUserName;
 
    $.getJSON(request_url, function(data) { ocr_callback(data); } );
}


function add_ocr() {
	var w = parseInt( self.proofreadPageEditWidth );
	if( !w ) {
		w = self.proofreadPageDefaultEditWidth;
	}
	if( !w ) {
		w = 1024; /* Default size in edit mode */
	}

	ocr_fetch_thumb_url( Math.min( w, self.proofreadPageWidth ), function( url, width, height ) {
		do_ocr(url);
	} );
}

function addOCRButton2(id,comment,source,onclick){
 
	var image = document.createElement("img");
	image.width = 46;
	image.id = id;
	image.height = 22;
	image.border = 0;
	image.className = "mw-toolbar-editbutton";
	image.style.cursor = "pointer";
	image.alt = "OCR";
	image.title = comment;
	image.src = source;
	image.onclick = onclick;

	var tb  = document.getElementById("toolbar"); 
	if(tb) {
		tb.appendChild(image);
        } else {
                if (id = 'wsOcr1')
                        image.src = '//upload.wikimedia.org/wikipedia/commons/c/c9/Toolbaricon_OCR.png';

                //append the button only after the toolbar is ready
                mw.loader.using('ext.wikiEditor.toolbar', function () {
                    var new_tb  = document.getElementById("wikiEditor-ui-toolbar"); 
        	    if(new_tb) {
	        	new_tb.childNodes[0].childNodes[1].appendChild(image);
                    }
                });
        }
}


function fraktur_ocr()
{
   self.wgContentLanguage = "de-f";
   add_ocr();
   self.wgContentLanguage = "de";
}


function addOCRButton()
{
    if(!self.proofreadPageIsEdit)
        return;

    if(self.wgContentLanguage == "de"){
       addOCRButton2("wsOcr1","Normale OCR","//upload.wikimedia.org/wikipedia/commons/e/e0/Button_ocr.png",add_ocr);
       addOCRButton2("wsOcr2","Fraktur OCR","//upload.wikimedia.org/wikipedia/commons/a/af/Button_Fractur_OCR.png", fraktur_ocr);
    } else {
        addOCRButton2("wsOcr1","Get the text by OCR","//upload.wikimedia.org/wikipedia/commons/e/e0/Button_ocr.png", add_ocr);
    }
}

if (!self.proofreadpage_disable_ocr) addOnloadHook(addOCRButton);