package cn.gotom.matcher;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Luke Taylor
 * Copied from Spring Security 3.0.7 RELEASE 
 */
public class RegexUrlMatcher implements UrlMatcher {
    private boolean requiresLowerCaseUrl = false;

    public Object compile(String path) {
        return Pattern.compile(path);
    }

    public void setRequiresLowerCaseUrl(boolean requiresLowerCaseUrl) {
        this.requiresLowerCaseUrl = requiresLowerCaseUrl;
    }

    public boolean pathMatchesUrl(Object compiledPath, String url) {
    	Pattern pattern = Pattern.compile((String)compiledPath);
        //Pattern pattern = (Pattern)compiledPath;

        return pattern.matcher(url).matches();
    }

    public String getUniversalMatchPattern() {
        return "/.*";
    }

    public boolean requiresLowerCaseUrl() {
        return requiresLowerCaseUrl;
    }
    
	public static void main(String[] args)
	{
		Map<String, String> resMap = new HashMap<String, String>();
		resMap.put("/admin/**/*.jsp", "ADMIN");
		resMap.put("/*.html", "ADMIN");
		UrlMatcher matcher = new RegexUrlMatcher();
		for (String res : resMap.keySet())
		{
			if (matcher.pathMatchesUrl(res, "/admin/aaa/bb/index.jsp"))
			{
				System.out.println(res);
			}
		}
	}    
}
