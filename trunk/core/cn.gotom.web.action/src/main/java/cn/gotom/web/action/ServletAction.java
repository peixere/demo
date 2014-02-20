package cn.gotom.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

public class ServletAction
{

	public HttpServletRequest getRequest()
	{
		return ServletActionContext.getRequest();
	}

	public HttpServletResponse getResponse()
	{
		return ServletActionContext.getResponse();
	}

	public String getUsername()
	{
		return ServletActionContext.getRequest().getRemoteUser();
	}

}
