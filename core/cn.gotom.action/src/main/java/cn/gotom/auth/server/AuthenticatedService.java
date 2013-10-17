package cn.gotom.auth.server;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import cn.gotom.auth.client.Authenticated;
import cn.gotom.auth.client.AuthenticatedClient;
import cn.gotom.injector.InjectorUtils;
import cn.gotom.service.AuthService;
import cn.gotom.servlet.AbstractConfigurationFilter;

public class AuthenticatedService extends AbstractConfigurationFilter
{
	protected final Logger log = Logger.getLogger(getClass());

	@Override
	public void destroy()
	{

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain arg2) throws IOException, ServletException
	{
		Authenticated authenticated = new Authenticated();
		try
		{
			String jsonString = AuthenticatedClient.convertStreamToString(request.getInputStream());
			authenticated = AuthenticatedClient.fromJsonString(jsonString);
			log.debug(authenticated);
			AuthService authService = InjectorUtils.getInstance(AuthService.class);
			boolean success = authService.isAuth(authenticated.getAppCode(), authenticated.getUsername(), authenticated.getUrl());
			authenticated.setResponse(success ? 200 : 403);
		}
		catch (Exception e)
		{
			authenticated.setMessage(e.getMessage());
		}
		finally
		{
			response.getWriter().println(authenticated.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException
	{

	}
}
