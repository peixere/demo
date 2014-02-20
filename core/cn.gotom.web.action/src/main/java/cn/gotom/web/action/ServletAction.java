package cn.gotom.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import cn.gotom.pojos.User;
import cn.gotom.service.UserService;

import com.google.inject.Inject;

public class ServletAction
{

	@Inject
	protected UserService userService;

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

	public User getLoginUser()
	{
		return userService.getByUsername(getUsername());
	}
}
