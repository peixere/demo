package cn.gotom.web.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.pojos.Organization;
import cn.gotom.pojos.Role;
import cn.gotom.pojos.Status;
import cn.gotom.pojos.User;
import cn.gotom.service.OrganizationService;
import cn.gotom.service.RoleService;
import cn.gotom.service.UserService;
import cn.gotom.util.PasswordEncoder;
import cn.gotom.util.StringUtils;
import cn.gotom.vo.TreeCheckedModel;

import com.google.inject.Inject;

@ParentPackage("json-default")
@Namespace(value = "/p")
@Action(value = "/user", results = { @Result(name = "success", type = "json") })
public class UserAction extends ServletAction
{
	protected final Logger log = Logger.getLogger(getClass());
	@Inject
	protected UserService userService;
	@Inject
	private RoleService roleService;
	@Inject
	PasswordEncoder passwordEncoder;
	@Inject
	private OrganizationService orgService;

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
		if (user.getOrganizations() != null)
		{
			for (Organization o : user.getOrganizations())
			{
				this.orgIds += o.getId() + ",";
				this.orgNames += o.getText() + ",";
			}
			if (orgIds.length() > 1)
			{
				orgIds = orgIds.substring(0, orgIds.length() - 1);
			}
			if (orgNames.length() > 1)
			{
				orgNames = orgNames.substring(0, orgNames.length() - 1);
			}
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
			if (User.ROOT.equals(user.getUsername()))
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
		User login = userService.getByUsername(getUsername());
		List<Organization> orgList = new ArrayList<Organization>();
		List<User> list = new ArrayList<User>();
		if (login.getUsername().equals(User.ROOT))
		{
			list = userService.findAll();
		}
		else
		{
			orgList = orgService.findAllByUser(login);
			list = userService.findAllByOrg(orgList);
		}
		for (User u : list)
		{
			if (u.getUsername().equals(User.ROOT))
			{
				list.remove(u);
				break;
			}
		}
		this.setData(list);
		return "success";
	}

	public void orgs()
	{
		// TreeModel
		// List<Role> roleList = roleService.f
		String parentId = ServletActionContext.getRequest().getParameter("id");
		if (user != null)
		{
			user = userService.get(user.getId());
		}
		User login = userService.getByUsername(getUsername());
		List<Organization> orgList = new ArrayList<Organization>();
		if (User.ROOT.equals(login.getUsername()))
		{
			orgList = orgService.findByParentId(parentId);
		}
		else
		{
			orgList = login.getOrganizations();
		}
		if (StringUtils.isNotEmpty(parentId))
		{
			orgList = orgService.findByParentId(parentId);
		}
		List<TreeCheckedModel> tree = new ArrayList<TreeCheckedModel>();
		List<Organization> selectOrgs = new ArrayList<Organization>();
		if (user != null)
		{
			if (user.getOrganizations() == null)
			{
				user.setOrganizations(orgService.findSelectedByUser(user));
			}
			selectOrgs = user.getOrganizations();
		}
		for (Organization o : orgList)
		{
			TreeCheckedModel e = new TreeCheckedModel();
			for (Organization check : selectOrgs)
			{
				if (o.getId().equals(check.getId()))
				{
					e.setChecked(true);
					break;
				}
			}
			e.setId(o.getId());
			e.setSort(o.getSort());
			e.setText(o.getText());
			tree.add(e);
		}
		ResponseUtils.toJSON(tree);
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
			List<Organization> userOrgs = new ArrayList<Organization>();
			if (StringUtils.isNotEmpty(orgIds))
			{
				String[] orgIdArray = orgIds.split(",");
				userOrgs = orgService.findByIds(Organization.class, orgIdArray);
			}
			user.setOrganizations(userOrgs);
			user.setRoles(userRoles);
			userService.save(user);
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