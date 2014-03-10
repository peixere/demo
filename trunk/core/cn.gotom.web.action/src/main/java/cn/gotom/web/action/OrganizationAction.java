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

import cn.gotom.pojos.Custom;
import cn.gotom.pojos.Organization;
import cn.gotom.service.CustomService;
import cn.gotom.service.OrganizationService;
import cn.gotom.util.StringUtils;
import cn.gotom.vo.JsonResponse;

import com.google.inject.Inject;

/**
 * 组织机构管理
 * 
 * @author <a href="mailto:pqixere@qq.com">裴绍国</a>
 * 
 */
@ParentPackage("json-default")
@Namespace(value = "/p")
@Action(value = "/organization", results = { @Result(name = "success", type = "json") })
public class OrganizationAction extends AbsPortalAction
{
	protected final Logger log = Logger.getLogger(getClass());

	private String id;

	private String parentId;

	private boolean success;

	@Inject
	private OrganizationService orgService;
	@Inject
	private CustomService customService;

	public void execute() throws IOException
	{
		Organization e = null;
		try
		{
			e = orgService.get(this.getId());
			if (!e.getCustom().getId().equalsIgnoreCase(this.getCurrentCustomId()))
			{
				e = null;
			}
		}
		catch (Exception ex)
		{
			log.warn(ex.getMessage());
		}
		if (e == null)
		{
			e = new Organization();
			Organization p = orgService.get(parentId);
			if (p != null)
			{
				e.setParentId(p.getId());
			}
		}
		toJSON(e);
	}

	public void tree() throws IOException
	{
		List<Organization> orgList = orgService.loadTree(getCurrentCustomId());
		//List<Organization> orgList = orgService.findByParentId(getCurrentCustomId(), this.getId());
		toJSON(orgList);
		// CommonUtils.toJSON(ServletActionContext.getRequest(), ServletActionContext.getResponse(), orgList, null, new String[] { "custom" });
	}

	public void list() throws IOException
	{
		List<Organization> menuList = orgService.findAllByParentId(getCurrentCustomId(), parentId);
		toJSON(menuList);
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
				if (!orgService.hasChildren(id))
				{
					orgService.removeById(id);
				}
				else
				{
					this.setSuccess(false);
				}
			}
		}
		return "success";
	}

	public void save()
	{
		JsonResponse jr = new JsonResponse();
		try
		{
			Organization o = new Organization();
			Map<String, String[]> params = ServletActionContext.getRequest().getParameterMap();
			BeanUtils.copyProperties(o, params);
			Organization old = orgService.get(o.getId());
			if (old != null)
			{
				o.setCustom(old.getCustom());
				if (old.getCustom() == null || !old.getCustom().getId().equalsIgnoreCase(this.getCurrentCustomId()))
				{
					jr.setSuccess(false);
					// ServletActionContext.getResponse().setStatus(403);
					jr.setData("数据访问权限不足");
					return;
				}
			}
			Custom custom = customService.get(getCurrentCustomId());
			o.setCustom(custom);
			orgService.save(o);
			jr.setSuccess(true);
		}
		catch (Exception e)
		{
			jr.setSuccess(false);
			jr.setData(e.getMessage());
			log.error(e.getMessage(), e);
		}
		finally
		{
			toJSON(jr);
		}
		// return "success";
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

	public String getParentId()
	{
		return parentId;
	}

	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}

}
