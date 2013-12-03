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
import cn.gotom.servlet.ResponseUtils;
import cn.gotom.util.PasswordEncoder;
import cn.gotom.util.StringUtils;
import cn.gotom.vo.TreeCheckedModel;

import com.google.inject.Inject;

@ParentPackage("json-default")
@Namespace(value = "/p")
@Action(value = "/user", results = { @Result(name = "success", type = "json") })
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
		if (user != null)
		{
			user = userService.get(user.getId());
		}
		if (user == null)
		{
			user = new User();
		}
		List<TreeCheckedModel> roleList = roleService.findAndChecked(user.getRoles());
		this.setData(roleList);
		return "success";
	}

	public void tree()
	{
		try
		{
			execute();
			List<TreeCheckedModel> roleList = roleService.findAndChecked(user.getRoles());
			if (User.admin.equals(user.getUsername()))
			{
				for (TreeCheckedModel m : roleList)
				{
					m.setChecked(true);
				}
			}
			this.setData(roleList);
			ResponseUtils.toJSON(roleList);
		}
		catch (Exception ex)
		{
			log.error(ex.getMessage(), ex);
		}
	}

	public String list()
	{
		List<User> list = userService.findAll();
		for (User u : list)
		{
			if (u.getUsername().equals(User.admin))
			{
				list.remove(u);
				break;
			}
		}
		this.setData(list);
		return "success";
	}

	public String save()
	{
		User old = userService.getByUsername(user.getUsername());
		if (old != null && !old.getId().equals(user.getId()))
		{
			this.setSuccess(false);
			this.setData(user.getUsername() + " 登录帐号已经被占用！");
		}
		else
		{
			if (old != null)
			{
				user.setPassword(old.getPassword());
			}
			else
			{
				PasswordEncoder passwordEncoder = new PasswordEncoder("MD5");
				user.setPassword(passwordEncoder.encode("123456"));
			}
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
		}
		return "success";
	}

	public String remove()
	{
		return settingStatus(Status.Delete);
	}
	
	public String banned()
	{
		return settingStatus(Status.Banned);
	}
	
	public String normal()
	{
		return settingStatus(Status.Normal);
	}

	private String settingStatus(Status status)
	{
		try
		{
			String[] ids = user.getId().split(",");
			for (String id : ids)
			{
				User user = userService.get(id.trim());
				if (user != null)
				{
					user.setStatus(status);
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

	public String getRoleIds()
	{
		return roleIds;
	}

	public void setRoleIds(String roleIds)
	{
		this.roleIds = roleIds;
	}

}