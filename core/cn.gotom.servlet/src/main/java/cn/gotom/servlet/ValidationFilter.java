package cn.gotom.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.gotom.service.AuthService;
import cn.gotom.service.DataInitializeService;
import cn.gotom.service.IUrlMatcher;
import cn.gotom.web.util.UrlUtils;
import cn.gotom.web.util.filter.AbstractConfigurationFilter;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ValidationFilter extends AbstractConfigurationFilter
{

	@Inject
	protected AuthService authService;

	@Inject
	protected DataInitializeService dataInitializeService;

	@Inject
	private IUrlMatcher urlMatcher;

	private String[] authenticationNones;

	private void initAuthenticationNone(FilterConfig filterConfig)
	{
		String none = getInitParameter(filterConfig, "authenticationNone");
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

	private boolean authenticationNone(String url)
	{
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
	
	public void init(FilterConfig filterConfig) throws ServletException
	{
		initAuthenticationNone(filterConfig);
		dataInitializeService.init();
	}

	public void destroy()
	{

	}

	public void doFilter(final ServletRequest sRequest, final ServletResponse sResponse, final FilterChain filterChain) throws IOException, ServletException
	{
		try
		{
			final HttpServletRequest request = (HttpServletRequest) sRequest;
			final HttpServletResponse response = (HttpServletResponse) sResponse;
			String url = UrlUtils.buildUrl(request);
			if (authenticationNone(url) || authService.isAuth(request.getRemoteUser(), url))
			{
				filterChain.doFilter(sRequest, sResponse);
			}
			else
			{
				log.warn(request.getRemoteUser() + " 403：" + url);
				response.setStatus(403);
				request.setAttribute("url", UrlUtils.buildUrl(request));
				request.getRequestDispatcher("/final/403.jsp").forward(request, response);
			}
		}
		finally
		{

		}
	}
}
