package cn.gotom.web.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.pojos.Status;
import cn.gotom.pojos.User;
import cn.gotom.service.RoleService;
import cn.gotom.service.Service;
import cn.gotom.service.UserService;
import cn.gotom.util.PasswordEncoder;
import cn.gotom.util.StringUtils;
import cn.gotom.vo.TreeCheckedModel;

import com.google.inject.Inject;

@ParentPackage("json-default")
@Namespace(value = "/p")
@Action(value = "/user", results = { @Result(name = "success", type = "json") })
public class UserAction extends AbsPortalAction
{
	protected final Logger log = Logger.getLogger(getClass());
	@Inject
	protected UserService userService;
	@Inject
	private RoleService roleService;
	@Inject
	PasswordEncoder passwordEncoder;

	@Inject
	private Service service;

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
		List<TreeCheckedModel> roleList = roleService.findAndChecked(getCurrentCustomId(), user.getRoles());
		this.setData(roleList);
		return "success";
	}

	public String list()
	{
		List<User> list = service.findUserByCustomId(getCurrentCustomId(), getCurrentUsername());
		this.setData(list);
		return "success";
	}

	public String save()
	{
		String[] roleIdArray = new String[0];
		if (StringUtils.isNotEmpty(roleIds))
		{
			roleIdArray = roleIds.split(",");
		}
		if (!service.saveUser(getCurrentUsername(), this.getCurrentCustomId(), user, roleIdArray))
		{
			this.setSuccess(false);
			this.setData(user.getUsername() + " 登录帐号已经被占用！");
		}
		return "success";
	}

	public String resetpass()
	{
		try
		{
			String[] ids = user.getId().split(",");
			for (String id : ids)
			{
				User user = userService.get(id.trim());
				if (user != null)
				{
					user.setPassword(passwordEncoder.encode("123456"));
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

	private String roleIds = "";
	private String orgIds = "";
	private String orgNames = "";

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

	public String getOrgIds()
	{
		return orgIds;
	}

	public void setOrgIds(String orgIds)
	{
		this.orgIds = orgIds;
	}

	public String getOrgNames()
	{
		return orgNames;
	}

	public void setOrgNames(String orgNames)
	{
		this.orgNames = orgNames;
	}

}