package cn.gotom.web.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.json.JSONCleaner;

import cn.gotom.pojos.User;
import cn.gotom.service.UserService;

import com.google.inject.Inject;

@ParentPackage("json-default")
public class WelcomeAction
{
	@Inject
	private UserService userService;

	private String userName;
	private String message;

	@Action(value = "/welcome", results = { @Result(name = "success", location = "/WEB-INF/jsp/welcome.jsp") })
	public String execute()
	{
		User user = userService.getByUsername(User.admin);
		message = user.getUsername() + "欢迎 " + userName + " !";
		return "success";
	}

	@Action(value = "/welcome/json", results = { @Result(name = "success", type = "json") })
	public String json()
	{
		System.out.println(userName + message);
		message = "欢迎 " + userName + " !";
		return "success";
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getUserName()
	{
		return userName;
	}

	public String getMessage()
	{
		return message;
	}
}
