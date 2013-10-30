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

import cn.gotom.pojos.Organization;
import cn.gotom.service.OrganizationService;
import cn.gotom.servlet.JsonAction;
import cn.gotom.util.StringUtils;

import com.google.inject.Inject;

@ParentPackage("json-default")
@Namespace(value = "/p")
@Action(value = "/organization", results = { @Result(name = "success", type = "json") })
public class OrganizationAction
{
	protected final Logger log = Logger.getLogger(getClass());

	private String id;

	private boolean success;

	@Inject
	private OrganizationService service;

	public void execute() throws IOException
	{
		Organization e = service.get(this.getId());
		JsonAction.writerToJSON(e);
	}

	public void fresh() throws IOException
	{
		Organization o = new Organization();
		JsonAction.writerToJSON(o);
	}

	public void tree() throws IOException
	{
		List<Organization> menuList = service.loadTree();
		if(menuList.size() == 0){
			Organization o = new Organization();
			o.setId(null);
			menuList.add(o);
		}
		JsonAction.writerToJSON(menuList);
	}

	public String remove()
	{
		String ids = getId();
		if (StringUtils.isNotEmpty(ids))
		{
			String[] idarray = ids.split(",");
			this.setSuccess(true);
			for (String id : idarray)
			{
				if (service.findByParentId(id).size() == 0)
				{
					service.remove(id);
				}
				else
				{
					this.setSuccess(false);
				}
			}
		}
		return "success";
	}

	public String save()
	{
		try
		{
			Organization o = new Organization();
			Map<String, String[]> params = ServletActionContext.getRequest().getParameterMap();
			BeanUtils.copyProperties(o, params);
			this.setSuccess(true);
			service.save(o);
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
