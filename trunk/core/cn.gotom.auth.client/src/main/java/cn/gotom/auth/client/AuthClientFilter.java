package cn.gotom.auth.client;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.gotom.servlet.AbstractConfigurationFilter;
import cn.gotom.servlet.UrlUtils;

public class AuthClientFilter extends AbstractConfigurationFilter
{

	private String appCode;

	private String authServiceUrl;

	public void init(FilterConfig filterConfig) throws ServletException
	{
		appCode = this.getInitParameter(filterConfig, "appCode", null);
		authServiceUrl = this.getInitParameter(filterConfig, "authServiceUrl", null);
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
			Authenticated authenticated = new Authenticated();
			authenticated.setAppCode(appCode);
			authenticated.setUrl(url);
			authenticated.setUsername(request.getRemoteUser());
			AuthClient authClient = new AuthClient();
			authenticated = authClient.auth(this.authServiceUrl, authenticated);
			if (authenticated.getResponse() == 200)
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
