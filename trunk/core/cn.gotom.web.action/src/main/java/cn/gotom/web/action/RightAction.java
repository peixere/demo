package cn.gotom.web.action;

import java.io.IOException;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.pojos.Right;
import cn.gotom.service.RightService;
import cn.gotom.servlet.JsonAction;
import cn.gotom.util.StringUtils;

import com.google.inject.Inject;

@ParentPackage("json-default")
public class RightAction extends JsonAction
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

	@Action(value = "/core/rightTree", results = { @Result(name = "success", type = "json") })
	public void tree() throws IOException
	{
		// if(rightService.findAll().size() == 0){
		//
		// }
		String sql = "select  t.id, t.text, t.component, t.description, t.type, t.iconCls, t.sort,t.leaf from resource t";
		if (StringUtils.isNullOrEmpty(id))
		{
			sql += " where t.parent_id is null";
		}
		else
		{
			sql += " where t.parent_id = '" + id + "'";
		}
		List<Right> menuList = rightService.query(Right.class, sql);
		for (Right r : menuList)
		{
			r.setExpanded(true);
			loadChildrencallback(r);
		}
		this.toJSON(menuList);
	}

	private void loadChildrencallback(Right right)
	{
		String sql = "select  t.id, t.text, t.component, t.description, t.type, t.iconCls, t.sort,t.leaf from resource t";
		sql += " where t.parent_id = '" + right.getId() + "'";
		List<Right> menuList = rightService.query(Right.class, sql);
		right.setChildren(menuList);
		for (Right r : menuList)
		{
			loadChildrencallback(r);
		}
	}
}
