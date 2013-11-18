package cn.gotom.web.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.pojos.Role;
import cn.gotom.pojos.Status;
import cn.gotom.pojos.User;
import cn.gotom.service.RoleService;
import cn.gotom.service.UserService;
import cn.gotom.util.StringUtils;

import com.google.inject.Inject;

@ParentPackage("json-default")
@Namespace(value = "/p")
@Action(value = "/User", results = { @Result(name = "success", type = "json") })
public class UserAction
{
	protected final Logger log = Logger.getLogger(getClass());
	@Inject
	private RoleService roleService;

	@Inject
	private UserService userService;

	public String json()
	{
		return "success";
	}

	public String execute()
	{
		if (user != null && StringUtils.isNotEmpty(user.getId()))
		{
			user = userService.get(user.getId());
		}
		if (user == null)
		{
			user = new User();
		}
		List<Role> hasRoles = user.getRoles();
		List<Role> roleList = roleService.findAllAndChecked(hasRoles);
		this.setData(roleList);
		return "success";
	}

	public String list()
	{
		List<User> list = userService.findAll();
		this.setData(list);
		return "success";
	}

	public String save()
	{
		List<Role> userRoles = new ArrayList<Role>();
		if (StringUtils.isNotEmpty(roleIds))
		{
			String[] roleIdArray = roleIds.split(",");
			List<Role> roleList = roleService.findAll();
			for (Role o : roleList)
			{
				for (String roleId : roleIdArray)
				{
					if (o.getId().equals(roleId.trim()))
					{
						userRoles.add(o);
						break;
					}
				}
			}
		}
		user.setRoles(userRoles);
		userService.save(user);
		return "success";
	}

	public String remove()
	{
		try
		{
			String[] ids = user.getId().split(",");
			for (String id : ids)
			{
				User user = userService.get(id.trim());
				if (user != null)
				{
					user.setStatus(Status.Delete);
					userService.save(user);
				}
			}
		}
		catch (Exception ex)
		{
			log.error("", ex);
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

}