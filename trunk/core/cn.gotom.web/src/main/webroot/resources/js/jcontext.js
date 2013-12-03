var jcontext = (function(){
    var context = {
	getQueryParam : function(name) 
	{
            var regex = RegExp('[?&]' + name + '=([^&]*)');
            var match = regex.exec(location.search) || regex.exec(path);
            return match && decodeURIComponent(match[1]);
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
    path = path.substring(path.lastIndexOf('//') + 2, path.length);
    if(path.indexOf('/') >= 0)
    {
	context.ctxp = path.substring(path.lastIndexOf('/'), path.length);
	context.domain = path.substring(0, path.indexOf('/'));;
    }
    else
    {
	context.domain = path;
    }
    return context;
})();