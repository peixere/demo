package cn.gotom.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.google.inject.Singleton;

import cn.gotom.auth.client.AuthClient;
import cn.gotom.auth.client.Authenticated;


public class AuthServiceServlet extends HttpServlet
{
	protected final Logger log = Logger.getLogger(getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			String jsonString = AuthClient.convertStreamToString(ServletActionContext.getRequest().getInputStream());
			JSON json = JSONSerializer.toJSON(jsonString);
			log.debug(jsonString);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass(Authenticated.class);
			Authenticated authenticated = (Authenticated) JSONSerializer.toJava(json, jsonConfig);
			json = net.sf.json.JSONSerializer.toJSON(authenticated);
			// ServletActionContext.getResponse().setContentType("text/html");
			response.getWriter().println(json.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
}
