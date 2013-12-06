package cn.gotom.sso.authentication;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import cn.gotom.sso.filter.AbstractConfigurationFilter;
import cn.gotom.sso.util.UrlUtils;

public class AuthenticatedClientFilter extends AbstractConfigurationFilter
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
			final HttpServletRequest httpRequest = (HttpServletRequest) sRequest;
			String url = UrlUtils.buildUrl(httpRequest);
			AuthenticatedRequest request = new AuthenticatedRequest();
			request.setAppCode(appCode);
			request.setUrl(url);
			request.setUsername(httpRequest.getRemoteUser());
			AuthenticatedClient authClient = new AuthenticatedClient();
			AuthenticatedResponse response = authClient.auth(this.authServiceUrl, request);
			if (response.getStatus() == 200)
			{
				filterChain.doFilter(sRequest, sResponse);
			}
			else
			{
				sResponse.getWriter().println(response);
				sResponse.getWriter().flush();
			}
		}
		finally
		{

		}
	}
}
