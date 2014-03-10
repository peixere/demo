package cn.gotom.web.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.gotom.pojos.Custom;
import cn.gotom.pojos.User;
import cn.gotom.sso.util.CommonUtils;
import cn.gotom.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class AbsPortalAction
{
	protected final Logger log = Logger.getLogger(getClass());

	public HttpServletRequest getRequest()
	{
		return ServletActionContext.getRequest();
	}

	public HttpServletResponse getResponse()
	{
		return ServletActionContext.getResponse();
	}

	public String getCurrentCustomId()
	{
		HttpServletRequest request = ServletActionContext.getRequest();
		String customId = (String) request.getSession().getAttribute(Custom.currentCustomId);
		if (StringUtils.isNullOrEmpty(customId))
		{
			customId = (String) request.getParameter("customId");
		}
		else
		{
			request.getSession().setAttribute(Custom.currentCustomId, customId);
		}
		if (StringUtils.isNullOrEmpty(customId))
		{
			User user = getCurrentUser();
			customId = user.getDefaultCustomId();
		}
		return customId;
	}

	protected <T> void toJSON(T value)
	{
		GsonBuilder gb = new GsonBuilder();
		gb.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
		Gson gson = gb.create();
		String json = gson.toJson(value);
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String encoing = request.getCharacterEncoding();
		if (CommonUtils.isEmpty(encoing))
		{
			encoing = "utf-8";
		}
		// response.setContentType("text/html;charset=" + encoing);
		response.setContentType("application/json;charset=" + encoing);
		try
		{
			response.getWriter().println(json);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			log.error("输出JSON异常 " + json);
		}
		finally
		{
			try
			{
				response.getWriter().flush();
				response.getWriter().close();
			}
			catch (IOException e)
			{
				log.error("输出JSON异常 " + json);
			}
		}
	}

	public String getCurrentUsername()
	{
		return ServletActionContext.getRequest().getRemoteUser();
	}

	public User getCurrentUser()
	{
		return (User) ServletActionContext.getRequest().getAttribute(User.CurrentLoginUser);
	}
}
