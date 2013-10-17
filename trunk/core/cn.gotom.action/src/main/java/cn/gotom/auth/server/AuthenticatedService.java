package cn.gotom.auth.server;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;

import cn.gotom.auth.client.AuthenticatedClient;
import cn.gotom.auth.client.Authenticated;
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
			JSON json = JSONSerializer.toJSON(jsonString);
			log.debug(jsonString);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass(Authenticated.class);
			authenticated = (Authenticated) JSONSerializer.toJava(json, jsonConfig);
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
			JSON json = net.sf.json.JSONSerializer.toJSON(authenticated);
			response.getWriter().println(json.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException
	{

	}
}
