package cn.gotom.web.action;

import java.io.IOException;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.pojos.Right;
import cn.gotom.service.RightService;
import cn.gotom.servlet.JsonAction;

import com.google.inject.Inject;

@ParentPackage("json-default")
@Action(value = "/right", results = { @Result(name = "success", type = "json") })
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

	public String execute() throws IOException
	{
		return "success";
	}

	@Action(value = "/core/rightTree", results = { @Result(name = "success", type = "json") })
	public void tree() throws IOException
	{
		List<Right> menuList = rightService.loadTree();
		this.toJSON(menuList);
	}

	@Action(value = "/core/rightload")
	public void load() throws IOException
	{
		Right right = rightService.get(id);
		this.toJSON(right);
	}
	@Action(value = "/core/right!save")
	public void save() throws IOException
	{
		Right right = rightService.get(id);
		this.toJSON(right);
	}
}
