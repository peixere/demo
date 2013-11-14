package cn.gotom.web.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.pojos.Role;
import cn.gotom.service.RightService;
import cn.gotom.service.RoleService;
import cn.gotom.servlet.ResponseUtils;
import cn.gotom.vo.JsonResponse;

import com.google.inject.Inject;

@ParentPackage("json-default")
@Namespace(value = "/p")
@Action(value = "/Role", results = { @Result(name = "success", type = "json") })
public class RoleAction
{
	protected final Logger log = Logger.getLogger(getClass());

	private Role role;

	@Inject
	private RightService rightService;

	@Inject
	private RoleService roleService;

	public void execute()
	{

	}

	public void list()
	{
		List<Role> roleList = roleService.findAll();
		JsonResponse json = new JsonResponse();
		json.setData(roleList);
		ResponseUtils.toJSON(json);
	}

	public Role getRole()
	{
		return role;
	}

	public void setRole(Role role)
	{
		this.role = role;
	}

}
