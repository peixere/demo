package cn.gotom.web.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

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

	private List<?> menuList;

	public List<?> getMenuList()
	{
		return menuList;
	}

	public void setMenuList(List<?> menuList)
	{
		this.menuList = menuList;
	}

	@Action(value = "/menu", results = { @Result(name = "success", type = "json") })
	public String menu()
	{
		String sql = "";
		if (StringUtils.isNullOrEmpty(id))
		{
			sql = "select  t.id, t.text, t.component, " + " t.description, t.type, t.iconCls, t.sort " + " from resource t where t.parent_id is null";
		}
		else
		{
			sql = "select  t.id, t.text, t.component, " + " t.description, t.type, t.iconCls, t.sort,t.leaf " + " from resource t where t.parent_id = '" + id + "'";
		}
		menuList = rightService.execute(sql);
		return "success";
	}
}
