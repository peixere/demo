package cn.gotom.web.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.pojos.Right;
import cn.gotom.service.RightService;
import cn.gotom.servlet.JsonAction;

import com.google.inject.Inject;

@ParentPackage("json-default")
@Action(value = "/right", results = { @Result(name = "success", type = "json") })
public class RightAction
{
	protected final Logger log = Logger.getLogger(getClass());

	private String id;

	private boolean success;

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
		JsonAction.writerToJSON(menuList);
	}

	@Action(value = "/core/rightload")
	public void load() throws IOException
	{
		Right right = rightService.get(id);
		JsonAction.writerToJSON(right);
	}

	@Action(value = "/core/right!save")
	public void save() throws IOException
	{
		Map<String, String[]> params = ServletActionContext.getRequest().getParameterMap();
		for (String key : params.keySet())
		{
			for (String v : params.get(key))
			{
				log.debug(key + "=" + v);
			}
		}
		// JsonAction.writerToJSON(this);
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public boolean isSuccess()
	{
		return success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}
}
