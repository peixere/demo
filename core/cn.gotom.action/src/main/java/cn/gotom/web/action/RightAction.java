package cn.gotom.web.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.pojos.Resource;
import cn.gotom.service.RightService;
import cn.gotom.util.StringUtils;

import com.google.inject.Inject;

@ParentPackage("json-default")
public class RightAction
{
	private String id;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	@Inject
	private RightService rightService;

	@Action(value = "/right", results = { @Result(name = "success", type = "json") })
	public String json()
	{
		return "success";
	}

	private List<Resource> menuList;

	public List<Resource> getMenuList()
	{
		return menuList;
	}

	public void setMenuList(List<Resource> menuList)
	{
		this.menuList = menuList;
	}

	@Action(value = "/menu", results = { @Result(name = "success", type = "json") })
	public String menu()
	{
		String sql = "select  t.id, t.text as name, t.component, t.description, t.type, t.iconCls as icon, t.sort from resource t";
		if (StringUtils.isNullOrEmpty(id))
		{
			sql += " where t.parent_id is null";
		}
		else
		{
			sql += " where t.parent_id = '" + id + "'";
		}
		menuList = rightService.query(Resource.class, sql);
		for (Resource r : menuList)
		{
			r.getId();
		}
		return "success";
	}
}
