package cn.gotom.web.action;

import java.util.ArrayList;
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
import cn.gotom.pojos.Right;
import cn.gotom.service.CustomService;
import cn.gotom.service.RightService;
import cn.gotom.service.model.RightChecked;
import cn.gotom.util.StringUtils;
import cn.gotom.vo.JsonResponse;

import com.google.inject.Inject;

@ParentPackage("json-default")
@Namespace(value = "/p")
@Action(value = "/custom", results = { @Result(name = "success", type = "json") })
public class CustomAction extends AbsPortalAction
{
	protected final Logger log = Logger.getLogger(getClass());

	private Custom custom;

	private String rightIds;

	@Inject
	private CustomService customService;

	@Inject
	private RightService rightService;

	public void execute()
	{
		if (custom != null)
		{
			custom = customService.get(custom.getId());
		}
		if (custom == null)
		{
			custom = new Custom();
		}
		JsonResponse jr = new JsonResponse();
		jr.setData(custom);
		jr.setSuccess(true);
		toJSON(jr);
	}

	public void tree()
	{
		JsonResponse jr = new JsonResponse();
		try
		{
			List<Right> rights = customService.findRights(custom.getId());
			List<RightChecked> rightList = rightService.loadCheckedTree(rights);
			jr.setData(rightList);
			jr.setSuccess(true);
		}
		catch (Exception ex)
		{
			jr.setData(ex.getMessage());
			jr.setSuccess(false);
			log.error(ex.getMessage(), ex);
		}
		toJSON(jr);
	}

	public void list()
	{
		List<Custom> list = customService.findAll();
		JsonResponse jr = new JsonResponse();
		jr.setData(list);
		jr.setSuccess(true);
		toJSON(jr);
	}

	public void save()
	{
		JsonResponse jr = new JsonResponse();
		try
		{
			custom = new Custom();
			Map<String, String[]> params = ServletActionContext.getRequest().getParameterMap();
			BeanUtils.copyProperties(custom, params);
			List<Right> roleRights = new ArrayList<Right>();
			if (StringUtils.isNotEmpty(rightIds))
			{
				String[] rightIdArray = rightIds.split(",");
				List<Right> rightList = rightService.findAll();
				for (Right right : rightList)
				{
					for (String rightId : rightIdArray)
					{
						if (right.getId().equals(rightId.trim()))
						{
							roleRights.add(right);
							break;
						}
					}
				}
			}
			customService.saveAndRight(custom, roleRights);
			jr.setSuccess(true);
		}
		catch (Exception ex)
		{
			jr.setData(ex.getMessage());
			jr.setSuccess(false);
			log.error(ex.getMessage(), ex);
		}
		toJSON(jr);
	}

	public void remove()
	{
		JsonResponse jr = new JsonResponse();
		try
		{
			String idstr = custom.getId().replaceAll(Custom.Default, "");
			String[] ids = idstr.split(",");
			customService.removeByIds(ids);
			jr.setSuccess(true);
		}
		catch (Exception ex)
		{
			jr.setData(ex.getMessage());
			jr.setSuccess(false);
			log.error(ex.getMessage(), ex);
		}
		toJSON(jr);
	}

	public Custom getCustom()
	{
		return custom;
	}

	public void setCustom(Custom custom)
	{
		this.custom = custom;
	}

	public String getRightIds()
	{
		return rightIds;
	}

	public void setRightIds(String rightIds)
	{
		this.rightIds = rightIds;
	}

}
