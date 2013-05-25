function ClickFlow(){
	var i;
	var num=document.ContentForm.Url.length - 1;
	for ( i=0; i<=num; i=i+1 ){
		if (document.ContentForm.Url.options[i].selected == 1 && document.ContentForm.Url.options[i].value !== "N/A"  ){
			window.open(document.ContentForm.Url.options[i].value ,"_self");
		}
	}
}
