var jcontext = (function(){
    var context = {
	getQueryParam : function(name) 
		{
            var regex = RegExp('[?&]' + name + '=([^&]*)');
            var match = regex.exec(location.search) || regex.exec(path);
            return match && decodeURIComponent(match[1]);
        },

        addQueryParam: function(url, name, value) {
            var path = url;
            if (value !== null && value.length > 0)
            {
                if (url.indexOf('?') >= 0)
                {
                    path = url + '&' + name + '=' + value;
                }
                else
                {
                    path = url + '?' + name + '=' + value;
                }
            }
            return path;
        },

        removeQueryParam: function(url, name) {
            var path = url;
			alert(name);
			var value = jcontext.getQueryParam(name);
			if (value !== null && value.length > 0)
            {
				var tmp = name+'='+value;
				alert(tmp);
				int index = path.indexOf(tmp);
                path = path.substring(0,index) + path.substring(index+tmp.length,path.length);
            }
            return path;
        },

        hasOption : function(opt, queryString) 
        {
            var s = queryString || location.search;
            var re = new RegExp('(?:^|[&?])' + opt + '(?:[=]([^&]*))?(?:$|[&])', 'i');
            var m = re.exec(s);
            return m ? (m[1] === undefined || m[1] === '' ? true : m[1]) : false;
        },

        getCookieValue : function(name)
        {
            var cookies = document.cookie.split('; '),
                i = cookies.length,
                cookie, value = '';
            while(i--) {
               cookie = cookies[i].split('=');
               if (cookie[0] === name) {
                   value = cookie[1];
               }
            }
            return value;
        },
        domain : '',
        ctxp : '',
        path : ''
    };
    var scriptEls = document.getElementsByTagName('script');
    var path = scriptEls[scriptEls.length - 1].src;
    var i = 3;
    while (i--) 
    {
        path = path.substring(0, path.lastIndexOf('/'));
    }    
    context.path = path;
    if(path.indexOf('//') > -1){
	path = path.substring(path.indexOf('//') + 2, path.length);
	if(path.indexOf('/') >= 0)
	{
	    context.ctxp = path.substring(path.indexOf('/'), path.length);
	    context.domain = path.substring(0, path.indexOf('/'));
	}
	else
	{
	    context.domain = path;
	}	
    }
    else
    {
	try{
	    var str = window.location.toString();
	    str = str.substring(str.indexOf('//') + 2, str.length);
	    domain = str.substring(0, str.indexOf('/'));
	}catch(error){
	    alert(error);
	}
    }    
    return context;
})();