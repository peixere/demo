package cn.gotom.web.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.pojos.Role;
import cn.gotom.service.RoleService;
import cn.gotom.servlet.ResponseUtils;
import cn.gotom.util.StringUtils;
import cn.gotom.vo.TreeCheckedModel;

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

	@Inject
	private RoleService roleService;

	public String execute()
	{
		if (role != null && StringUtils.isNotEmpty(role.getId()))
		{
			role = roleService.get(role.getId());
			if (role == null)
			{
				role = new Role();
			}
		}
		return "success";
	}

	public void tree()
	{
		List<TreeCheckedModel> menuList = roleService.loadRightTree(role.getId());
		ResponseUtils.toJSON(menuList);
	}

	public String list()
	{
		List<Role> roleList = roleService.findAll();
		this.setData(roleList);
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

}
