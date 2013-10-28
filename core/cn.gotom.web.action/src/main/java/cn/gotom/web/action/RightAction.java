package cn.gotom.web.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.pojos.Right;
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

	@Action(value = "/menu", results = { @Result(name = "success", type = "json") })
	public void menu() throws IOException
	{
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
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		//response.setCharacterEncoding("utf-8");
		response.getWriter().println(JSONArray.fromObject(menuList).toString());
		response.getWriter().flush();
		response.getWriter().close();
	}
}
