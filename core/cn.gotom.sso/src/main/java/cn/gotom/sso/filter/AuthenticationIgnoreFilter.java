package cn.gotom.sso.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import cn.gotom.matcher.PathMatcher;
import cn.gotom.matcher.PathMatcherAnt;
import cn.gotom.sso.util.UrlUtils;

public abstract class AuthenticationIgnoreFilter extends AbstractCommonFilter
{
	protected final PathMatcher urlMatcher = new PathMatcherAnt();

	/**
	 * 忽列验证的路径
	 */
	private String[] authenticationNones;

	private void initIgnore(FilterConfig filterConfig)
	{
		String none = getInitParameter(filterConfig, "authenticationNone", null);
		if (none != null)
		{
			none = none.trim().replace("；", ";");
			none = none.replace(",", ";");
			none = none.replace("，", ";");
			none = none.replace("\n", ";");
			authenticationNones = none.trim().split(";");
			for (int i = 0; i < authenticationNones.length; i++)
			{
				authenticationNones[i] = authenticationNones[i].trim();
			}
		}
	}

	protected boolean isIgnore(HttpServletRequest request)
	{
		String url = UrlUtils.buildUrl(request);

		if (this.serverLoginUrl.equals(url))
		{
			return true;
		}
		else
		{
			String uri = UrlUtils.buildFullRequestURI(request);
			if (this.serverLoginUrl.equals(uri))
			{
				return true;
			}
		}
		if (authenticationNones != null)
		{
			for (String pattern : authenticationNones)
			{
				if (urlMatcher.pathMatchesUrl(pattern.trim(), url))
				{
					return true;
				}
			}
		}
		return false;
	}

	protected void initInternal(final FilterConfig filterConfig) throws ServletException
	{
		super.initInternal(filterConfig);
		initIgnore(filterConfig);
	}

	public String[] getAuthenticationNones()
	{
		return authenticationNones;
	}

	public void setAuthenticationNones(String[] authenticationNones)
	{
		this.authenticationNones = authenticationNones;
	}

}
