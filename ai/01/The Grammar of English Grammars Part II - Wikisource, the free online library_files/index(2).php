/**********************
*** Automatically generate page footer from values in {{header}}
*** by [[user:GrafZahl]] and [[user:Tpt]]
**********************/

$( document ).ready( function() {
	if( mw.config.get( 'wgNamespaceNumber' ) !== 0 || mw.util.getParamValue( 'match' ) !== null ) {
		return;
	}
	var $nofooterElt = $( '#nofooter' );
	var $hp = $( '#headerprevious' );
	var $hn = $( '#headernext' );
 	var $contentElt = $( '#bodyContent' );
	if( $contentElt.length === 0 || ($hp.length === 0 && $hn.length === 0) || $nofooterElt.length !== 0 ) {
		return; 
	}

	var footer = '<div class="footertemplate ws-noexport" id="footertemplate" style="margin-top:1em; clear:both;">';
	footer += '<div style="width: 100%; padding-left:0px; padding-right:0px; background-color:transparent;">';
	if( $hp.length !== 0 ) {
		footer += '<div style="text-align:left; float:left; max-width:40%;"><span id="footerprevious">' + $hp.html() + '</span></div>';
	}
	if( $hn.length !== 0 ) {
		footer += '<div style="text-align:right; float:right; max-width:40%;"><span id="footernext">' + $hn.html() + '</span></div>';
	}
	footer += '<div style="text-align:center; margin-left: 25%; margin-right: 25%;"><a href="#top">' + ws_msg( '▲' ) + '</a></div>';
	footer += '</div><div style="clear:both;"></div></div>';

	$headerTemplate = $( '#headertemplate' );
	if( $headerTemplate.length !== 0 ) {
		$headerTemplate.parent().append( footer );
	} else {
		var $catlinksElt = $( '#catlinks' );
		if( $catlinksElt.length !== 0 ) { // place footer before category box
			$catlinksElt.before( footer );
		} else {
			$contentElt.append( footer );
		}
	}
} );