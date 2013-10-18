package cn.gotom.auth.server;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import cn.gotom.auth.client.AuthenticatedClient;
import cn.gotom.auth.client.AuthenticatedRequest;
import cn.gotom.auth.client.AuthenticatedResponse;
import cn.gotom.service.AuthService;
import cn.gotom.servlet.AbstractConfigurationFilter;

import com.google.inject.Inject;

public class AuthenticatedService extends AbstractConfigurationFilter
{
	protected final Logger log = Logger.getLogger(getClass());

	@Override
	public void destroy()
	{

	}

	@Inject
	protected AuthService authService;

	@Override
	public void doFilter(ServletRequest sRequest, ServletResponse sResponse, FilterChain arg2) throws IOException, ServletException
	{
		AuthenticatedRequest request = new AuthenticatedRequest();
		AuthenticatedResponse response = null;
		try
		{
			String jsonString = AuthenticatedClient.convertStreamToString(sRequest.getInputStream());
			request = AuthenticatedClient.fromJsonString(AuthenticatedRequest.class, jsonString);
			response = new AuthenticatedResponse(request);
			boolean success = authService.isAuth(request.getAppCode(), request.getUsername(), request.getUrl());
			response.setStatus(success ? 200 : 403);
		}
		catch (Exception e)
		{
			if (response == null)
			{
				response = new AuthenticatedResponse(request);
			}
			response.setStatus(505);
			response.setMessage(e.getMessage());
		}
		finally
		{
			sResponse.getWriter().println(response.toString());
			sResponse.getWriter().flush();
			sResponse.getWriter().close();
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException
	{

	}
}
