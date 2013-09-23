package cn.gotom.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.gotom.service.DataInitializeService;
import cn.gotom.service.AuthService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AuthFilter implements Filter
{
	protected final Logger log = Logger.getLogger(getClass());

	@Inject
	protected AuthService authService;

	@Inject
	protected DataInitializeService dataInitializeService;

	public void init(FilterConfig filterConfig) throws ServletException
	{
		Enumeration<String> names = filterConfig.getInitParameterNames();
		while (names.hasMoreElements())
		{
			String key = names.nextElement();
			log.info(key + "=" + filterConfig.getInitParameter(key));
		}
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
			if (authService.isAuth(request.getRemoteUser(), url))
			{
				filterChain.doFilter(sRequest, sResponse);
			}
			else
			{
				request.getRequestDispatcher("/final/403.jsp").forward(request, response);
			}
		}
		finally
		{

		}
	}
}
