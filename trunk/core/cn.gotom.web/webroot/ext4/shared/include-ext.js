var ctxp = '';
(function() {
    function getQueryParam(name) {
        var regex = RegExp('[?&]' + name + '=([^&]*)');

        var match = regex.exec(location.search) || regex.exec(path);
        return match && decodeURIComponent(match[1]);
    }

    function hasOption(opt, queryString) {
        var s = queryString || location.search;
        var re = new RegExp('(?:^|[&?])' + opt + '(?:[=]([^&]*))?(?:$|[&])', 'i');
        var m = re.exec(s);

        return m ? (m[1] === undefined || m[1] === '' ? true : m[1]) : false;
    }

    function getCookieValue(name){
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
    }

    var scriptEls = document.getElementsByTagName('script');
    var path = scriptEls[scriptEls.length - 1].src,
        rtl = getQueryParam('rtl'),
        theme = getQueryParam('theme') || 'neptune',
        includeCSS = !hasOption('nocss', path),
        neptune = (theme === 'neptune'),
        repoDevMode = getCookieValue('ExtRepoDevMode'),
        suffix = [],
        i = 3,
        neptunePath;

    rtl = rtl && rtl.toString() === 'true';
    while (i--) {
        path = path.substring(0, path.lastIndexOf('/'));
    }
    ctxp = path;
    if (theme && theme !== 'classic') {
        suffix.push(theme);
    }
    if (rtl) {
        suffix.push('rtl');
    }
    suffix = (suffix.length) ? ('-' + suffix.join('-')) : '';
    //var prefix = 'http://cdn.sencha.com/ext/gpl/4.2.1';
    var prefix = path + "/ext4/ext4.2.1"
    //var prefix = path + "/ext/4.2.1"
    if (includeCSS) {
        document.write('<link rel="stylesheet" type="text/css" href="' + prefix + '/resources/css/ext-all' + suffix + '-debug.css"/>');
    }
    document.write('<script type="text/javascript" src="' + prefix + '/ext-all' + (rtl ? '-rtl' : '') + '.js"></script>');

})();