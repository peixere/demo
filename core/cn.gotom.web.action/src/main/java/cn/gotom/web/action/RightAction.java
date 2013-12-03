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
import cn.gotom.service.model.RightTree;
import cn.gotom.servlet.ResponseUtils;
import cn.gotom.util.StringUtils;

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
		ResponseUtils.toJSON(right);
	}

	public void fresh() throws IOException
	{
		Right right = new Right();
		ResponseUtils.toJSON(right);
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
		ResponseUtils.toJSON(menuList);
	}

	public void list() throws IOException
	{
		List<Right> list = rightService.findAll();
		for (Right r : list)
		{
			r.setRoles(null);
		}
		ResponseUtils.toJSON(list);
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
