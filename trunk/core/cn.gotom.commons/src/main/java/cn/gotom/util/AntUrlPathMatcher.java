package cn.gotom.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AntUrlPathMatcher implements UrlMatcher
{
	private boolean requiresLowerCaseUrl = true;
	private PathMatcher pathMatcher = new AntPathMatcher();

	public AntUrlPathMatcher()
	{
		this(true);
	}

	public AntUrlPathMatcher(boolean requiresLowerCaseUrl)
	{
		this.requiresLowerCaseUrl = requiresLowerCaseUrl;
	}

	public Object compile(String path)
	{
		if (requiresLowerCaseUrl)
		{
			return path.toLowerCase(Locale.getDefault());
		}

		return path;
	}

	public void setRequiresLowerCaseUrl(boolean requiresLowerCaseUrl)
	{
		this.requiresLowerCaseUrl = requiresLowerCaseUrl;
	}

	public boolean pathMatchesUrl(Object path, String url)
	{
		if ("/**".equals(path) || "**".equals(path))
		{
			return true;
		}
		return pathMatcher.match((String) path, url);
	}

	public String getUniversalMatchPattern()
	{
		return "/**";
	}

	public boolean requiresLowerCaseUrl()
	{
		return requiresLowerCaseUrl;
	}

	public String toString()
	{
		return getClass().getName() + "[requiresLowerCase='" + requiresLowerCaseUrl + "']";
	}

	public static void main(String[] args)
	{
		Map<String, String> resMap = new HashMap<String, String>();
		resMap.put("/admin/**/*.jsp", "ADMIN");
		resMap.put("/*.html", "ADMIN");
		UrlMatcher matcher = new AntUrlPathMatcher();
		for (String res : resMap.keySet())
		{
			if (matcher.pathMatchesUrl(res, "/admin/aaa/bb/index.jsp"))
			{
				System.out.println(res);
			}
		}
	}
}
