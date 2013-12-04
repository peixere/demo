package cn.gotom.web.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.google.inject.Inject;

import cn.gotom.pojos.User;
import cn.gotom.service.UserService;

public class HelloAction
{
	private User user;
	
	@Inject
	private UserService userService;

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}
	
	@Action(value = "/hello", results = { @Result(name = "success", location = "/WEB-INF/jsp/hello.jsp") })
	public String hello()
	{
		user = userService.get("username", User.ROOT);
		return "success";
	}
	
	@Action(value = "/helloSave", results = { @Result(name = "success", location = "/WEB-INF/jsp/hello.jsp") })
	public String save()
	{
		User old = userService.get("username", User.ROOT);
		old.setName(user.getName());
		userService.save(old);
		return "success";
	}
}
