package cn.gotom.web.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.pojos.Right;
import cn.gotom.service.RightService;
import cn.gotom.servlet.JsonAction;

import com.google.inject.Inject;

@ParentPackage("json-default")
// @Namespaces(value = {@Namespace(value="/p")})
@Namespace(value = "/p")
@Action(value = "/right", results = { @Result(name = "success", type = "json") })
public class RightAction
{
	protected final Logger log = Logger.getLogger(getClass());

	private String id;

	private boolean success;

	@Inject
	private RightService rightService;

	public void execute() throws IOException
	{
		Right right = rightService.get(this.getId());
		JsonAction.writerToJSON(right);
	}

	public void tree() throws IOException
	{
		List<Right> menuList = rightService.loadTree();
		JsonAction.writerToJSON(menuList);
	}

	public String remove() 
	{
		rightService.remove(getId());
		return "success";
	}
	
	public String save()
	{
		try
		{
			Right right = new Right();
			Map<String, String[]> params = ServletActionContext.getRequest().getParameterMap();
			BeanUtils.copyProperties(right, params);
			this.setSuccess(true);
			rightService.save(right);
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
		return "success";
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
