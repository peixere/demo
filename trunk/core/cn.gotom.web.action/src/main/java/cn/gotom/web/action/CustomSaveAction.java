package cn.gotom.web.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;

import cn.gotom.pojos.Custom;
import cn.gotom.pojos.Right;
import cn.gotom.service.CustomService;
import cn.gotom.service.RightService;
import cn.gotom.util.StringUtils;
import cn.gotom.vo.JsonResponse;

import com.google.inject.Inject;

@Namespace(value = "/p")
@Action(value = "/customSave")
@InterceptorRefs(value = { @InterceptorRef("fileUploadStack") })
public class CustomSaveAction extends AbsPortalAction
{
	protected final Logger log = Logger.getLogger(getClass());

	private Custom custom;
	private File topbg;
	private String topbgFileName;
	private File logo;
	private String logoFileName;
	private String rightIds;

	@Inject
	private CustomService customService;

	@Inject
	private RightService rightService;

	public void execute()
	{
		save();
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

	public Custom getCustom()
	{
		return custom;
	}

	public void setCustom(Custom custom)
	{
		this.custom = custom;
	}

	public File getTopbg()
	{
		return topbg;
	}

	public void setTopbg(File topbg)
	{
		this.topbg = topbg;
	}

	public String getTopbgFileName()
	{
		return topbgFileName;
	}

	public void setTopbgFileName(String topbgFileName)
	{
		this.topbgFileName = topbgFileName;
	}

	public File getLogo()
	{
		return logo;
	}

	public void setLogo(File logo)
	{
		this.logo = logo;
	}

	public String getLogoFileName()
	{
		return logoFileName;
	}

	public void setLogoFileName(String logoFileName)
	{
		this.logoFileName = logoFileName;
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
