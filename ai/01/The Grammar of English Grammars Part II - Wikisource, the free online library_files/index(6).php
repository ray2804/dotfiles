/* author : ThomasV, Phe */

// Record all existing dynamic entry in this page associated with their from/to parameter
var map_links = {};

function dict_entry_callback(res) {
    var item = document.getElementById("dict_entry");
    if (!item)
        return;
    var res2 = eval("0,"+res);
    var txt = res2.parse.text['*'];

    item.innerHTML = txt;

    var $node = $("#dict_entry > div:first-child > p:first-child");
    if ($node.size() && !$node.text()) {
        $node.children().prependTo($("#dict_entry > div:first-child > p:nth-child(2)"));
    }

    self.pagenum_ml = get_elements_by_classname( 'pagenum', 'span' );
    for (var i = self.pagenum_ml.length - 1; i >= 0; i--) {
        var a = self.pagenum_ml[i];
        var num = decodeURI( a.id ); //.replace(/\./g,'%')
        if (num==".CE.9E")
            num="Ξ";
        var page = a.title;
        var pagetitle = decodeURI( page ); //.replace('?','%3F').replace(/\.2/g,'%2').replace("%2F","/")
        pagekey = pagetitle.replace(/ /g,"_");
        var page_url = wgArticlePath.replace("$1", pagekey);
        var ll = a.parentNode.nextSibling;
        if (ll && ll.tagName=="A" && ll.className=="new")
            class_str=" class=\"new\" "; else class_str="";
        var link_str = "<a href=\""+page_url+"\"" + class_str + " title=\""+pagetitle+"\">"+num+"</a>"
        a.innerHTML = "&#x0020;<span class=\"pagenumber noprint\" style=\"color:#666666; display:inline; margin:0px; padding:0px;\">[<b>"+link_str+"</b>]</span>&#x0020;";
    }

    var mlist = document.getElementById("dynamic_links");
    if (!mlist)
        return;
    var index = mlist.title;
    var self_url = encodeURI(mw.config.get('wgPageName'));
    $('#dict_entry a')
       .filter(function (index) { return $(this).attr('href').indexOf('/wiki/' + self_url) == 0; })
         .each(function(idx) {
             var title = $(this).attr('href').split('#')[1];
             var link_params = map_links[title];
             if (link_params) {
                 title = decodeURIComponent(title.replace(/\.([0-9A-F][0-9A-F])/g, '%$1'));
                 $(this).attr('href', "javascript:show_dict_entry(\""+index+"\","+link_params[0]+","+link_params[1]+",\""+title+"\");");
             }
         });
}
 
function show_dict_entry(index,from,to,title) {
    var item = document.getElementById("dict_entry");
    if (!item)
        return;
    item.innerHTML="…";
    var fs="fromsection=\""+title+"\"";
    var ts="tosection=\""+title+"\"";
    var url = wgServer+wgScriptPath+'/api.php?format=json&action=parse&title=' + mw.config.get('wgPageName') + '&text=<pages index="'+index+'" from='+from+' to='+to+' '+fs+' '+ts+' /> <references/>';
    var api=sajax_init_object();
    api.open("GET",url,true);
    api.onreadystatechange=function(){ if (api.readyState == 4) /*if (api.status==200)*/ dict_entry_callback(api.responseText);};
    api.send(null);
    location.href = document.URL.split('#')[0] + '#'+ escape(encodeURI( title.replace(/ /g,'_')).replace(/%/g,'.') ).replace(/%/g,'.');
}

function dl_links() {
    var mlist = document.getElementById("dynamic_links");
    if (!mlist)
        return;
    var index = mlist.title;
    var url_title = document.URL.split('#')[1]; 
    $('a[title="DL"]').each(function (item_index, item) {
        var title = item.innerHTML;
        var dl_ref = item.href.split('#')[1];
        var m = dl_ref.split(":");
        var m_from = parseInt(m[0]); 
        var m_to = parseInt(m[1]);
        map_links[encodeURIComponent(title).replace(/%([0-9A-F][0-9A-F])/g, '.$1')] = [ m_from, m_to ];
        item.href = "javascript:show_dict_entry(\""+index+"\","+m_from+","+m_to+",\""+title+"\");";
        item.title = title;
        if (url_title == escape(encodeURI( title.replace(/ /g,'_')).replace(/%/g,'.') ).replace(/%/g,'.'))
            show_dict_entry(index, m_from, m_to,title);
    });
}

addOnloadHook(dl_links);