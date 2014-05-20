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
import cn.gotom.pojos.CustomRight;
import cn.gotom.pojos.Right;
import cn.gotom.service.CustomService;
import cn.gotom.service.RightService;
import cn.gotom.service.model.RightTree;
import cn.gotom.util.StringUtils;

import com.google.inject.Inject;

@ParentPackage("json-default")
@Namespace(value = "/p")
@Action(value = "/right", results = { @Result(name = "success", type = "json") })
public class RightAction extends AbsPortalAction
{
	protected final Logger log = Logger.getLogger(getClass());

	private String id;

	private boolean success;

	@Inject
	private RightService rightService;
	@Inject
	private CustomService customService;

	public void execute() throws IOException
	{
		Right right = rightService.get(this.getId());
		if (right == null)
		{
			right = new Right();
		}
		toJSON(right, "roles");
	}

	public void fresh() throws IOException
	{
		Right right = new Right();
		toJSON(right);
	}

	public void tree() throws IOException
	{
		List<RightTree> menuList = rightService.loadTree();
		if (menuList.size() == 0)
		{
			Right o = new Right();
			o.setText("系统管理");
			rightService.save(o);
			menuList = rightService.loadTree();
		}
		toJSON(menuList);
	}

	public void list() throws IOException
	{
		List<Right> list = rightService.findAll();
		for (Right r : list)
		{
			r.setRoles(null);
		}
		toJSON(list);
	}

	public String remove()
	{
		String ids = getId();
		if (StringUtils.isNotEmpty(ids))
		{
			String[] idarray = ids.split(",");
			this.setSuccess(true);
			for (int i = idarray.length - 1; i >= 0; i--)
			{
				String id = idarray[i].trim();
				if (rightService.findByParentId(id).size() == 0)
				{
					customService.removeCustomRight(id);
					rightService.removeById(id);
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
			Right right = new Right();
			Map<String, String[]> params = ServletActionContext.getRequest().getParameterMap();
			BeanUtils.copyProperties(right, params);
			this.setSuccess(true);
			Right old = rightService.get(right.getId());
			if (old != null)
			{
				right.setRoles(old.getRoles());
			}
			rightService.save(right);
			CustomRight cr = customService.getCustomRight(right.getId(), this.getCurrentCustomId());
			if (cr == null)
			{
				cr = new CustomRight();
				cr.setCustom(new Custom());
				cr.getCustom().setId(this.getCurrentCustomId());
				cr.setRight(right);
				customService.persist(cr);
			}
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
