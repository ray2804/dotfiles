// Adds a "Download as EPUB" link to the sidebar
//
// Generates and starts a download of the current page as an EPUB file.
// Subpages of the current page (if any) are included as well.
//
// Uses the WSExport tool on Toolserver:
// http://toolserver.org/~tpt/wsexport/book.php
//
// See Oldwikisource:Wikisource:WSexport for more information
// Bug reports should go to fr:Wikisource:Wsexport
// Tool source code at https://github.com/wsexport
// 
if (wgNamespaceNumber == 0) {
  $(document).ready( function () {
    mw.util.addPortletLink(
      'p-coll-print_export',
      'http://wsexport.wmflabs.org/tool/book.php?lang=en&format=epub&page=' + mw.config.get('wgPageName'),
      'Download as EPUB',
      'n-epubExport',
      'Download an EPUB version of this page',
      '',
      '#t-print'); 
  });
}