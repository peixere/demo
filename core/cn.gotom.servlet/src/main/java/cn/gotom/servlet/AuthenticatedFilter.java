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
public class AuthenticatedFilter extends AbstractConfigurationFilter
{

	@Inject
	protected AuthService authService;

	@Inject
	protected DataInitializeService dataInitializeService;

	@Inject
	private IUrlMatcher urlMatcher;

	private String[] authenticatedNones;

	public void init(FilterConfig filterConfig) throws ServletException
	{
		String authenticatedNone = getInitParameter(filterConfig, "authenticatedNone");
		if (authenticatedNone != null)
		{
			authenticatedNone = authenticatedNone.trim().replace("；", ";");
			authenticatedNone = authenticatedNone.replace(",", ";");
			authenticatedNone = authenticatedNone.replace("，", ";");
			authenticatedNone = authenticatedNone.replace("\n", ";");
			authenticatedNones = authenticatedNone.trim().split(";");
			for (int i = 0; i < authenticatedNones.length; i++)
			{
				authenticatedNones[i] = authenticatedNones[i].trim();
			}
		}
		dataInitializeService.init();
	}

	public void destroy()
	{

	}

	private boolean none(String url)
	{
		if (authenticatedNones != null)
		{
			for (String pattern : authenticatedNones)
			{
				if (urlMatcher.pathMatchesUrl(pattern.trim(), url))
				{
					return true;
				}
			}
		}
		return false;
	}

	public void doFilter(final ServletRequest sRequest, final ServletResponse sResponse, final FilterChain filterChain) throws IOException, ServletException
	{
		try
		{
			final HttpServletRequest request = (HttpServletRequest) sRequest;
			final HttpServletResponse response = (HttpServletResponse) sResponse;
			String url = UrlUtils.buildUrl(request);
			if (none(url) || authService.isAuth(request.getRemoteUser(), url))
			{
				filterChain.doFilter(sRequest, sResponse);
			}
			else
			{
				log.warn(request.getRemoteUser() + " 403：" + url);
				response.setStatus(403);
				request.getRequestDispatcher("/final/403.jsp").forward(request, response);
			}
		}
		finally
		{

		}
	}
}
