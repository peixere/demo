package cn.gotom.web.action;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.pojos.User;
import cn.gotom.service.UserService;
import cn.gotom.util.PasswordEncoder;

import com.google.inject.Inject;

@ParentPackage("json-default")
@Namespace(value = "/p")
@Action(value = "/password", results = { @Result(name = "success", type = "json") })
public class UserPasswordAction
{
	protected final Logger log = Logger.getLogger(getClass());

	@Inject
	private UserService userService;

	public String execute()
	{
		if (user != null)
		{
			user = userService.get(user.getId());
		}
		if (user == null)
		{
			user = new User();
		}
		return "success";
	}

	public String save()
	{
		User old = userService.getByUsername(user.getUsername());
		if (old != null)
		{
			PasswordEncoder passwordEncoder = new PasswordEncoder("MD5");
			old.setPassword(passwordEncoder.encode(user.getPassword()));
			userService.save(old);
		}
		return "success";
	}

	private User user;

	private boolean success = true;

	private Object data;

	private String roleIds;

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public boolean isSuccess()
	{
		return success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	public Object getData()
	{
		return data;
	}

	public void setData(Object data)
	{
		this.data = data;
	}

	public String getRoleIds()
	{
		return roleIds;
	}

	public void setRoleIds(String roleIds)
	{
		this.roleIds = roleIds;
	}

}