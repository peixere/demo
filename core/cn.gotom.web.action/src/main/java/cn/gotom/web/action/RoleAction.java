package cn.gotom.web.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.pojos.Right;
import cn.gotom.pojos.Role;
import cn.gotom.service.RightService;
import cn.gotom.service.RoleService;
import cn.gotom.service.model.RightChecked;
import cn.gotom.servlet.ResponseUtils;
import cn.gotom.util.StringUtils;

import com.google.inject.Inject;

@ParentPackage("json-default")
@Namespace(value = "/p")
@Action(value = "/Role", results = { @Result(name = "success", type = "json") })
public class RoleAction
{
	protected final Logger log = Logger.getLogger(getClass());

	private Role role;

	private boolean success = true;

	private Object data;

	private String rightIds;

	@Inject
	private RoleService roleService;

	@Inject
	private RightService rightService;

	public String execute()
	{
		if (role != null && StringUtils.isNotEmpty(role.getId()))
		{
			role = roleService.get(role.getId());

		}
		if (role == null)
		{
			role = new Role();
		}
		return "success";
	}

	public void tree()
	{
		try
		{
			execute();
			List<RightChecked> rightList = rightService.loadCheckedTree(role.getRights());
			this.setData(rightList);
			ResponseUtils.toJSON(rightList);
		}
		catch (Exception ex)
		{
			log.error(ex.getMessage(), ex);
		}
	}

	public String list()
	{
		List<Role> roleList = roleService.findAll();
		this.setData(roleList);
		return "success";
	}

	public String save()
	{
		List<Right> roleRights = new ArrayList<Right>();
		if (StringUtils.isNotEmpty(rightIds))
		{
			String[] rightIdArray = rightIds.split(",");
			List<Right> rightList = rightService.findAll();
			for (Right right : rightList)
			{
				for (String rightId : rightIdArray)
				{
					if (right.getId().equals(rightId.trim()))
					{
						roleRights.add(right);
						break;
					}
				}
			}
		}
		role.setRights(roleRights);
		roleService.save(role);
		return "success";
	}

	public String remove()
	{
		try
		{
			String[] ids = role.getId().split(",");
			for (String id : ids)
			{
				Role role = roleService.get(id.trim());
				if (role != null)
					roleService.remove(role);
			}
		}
		catch (Exception ex)
		{
			log.error("", ex);
		}
		return "success";
	}

	public Role getRole()
	{
		return role;
	}

	public void setRole(Role role)
	{
		this.role = role;
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

	public String getRightIds()
	{
		return rightIds;
	}

	public void setRightIds(String rightIds)
	{
		this.rightIds = rightIds;
	}

}
