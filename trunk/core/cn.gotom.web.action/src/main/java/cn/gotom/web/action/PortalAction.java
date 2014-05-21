package cn.gotom.web.action;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.pojos.Custom;
import cn.gotom.service.CustomService;

import com.google.inject.Inject;

@Namespace(value = "/")
@Action(value = "p", results = { @Result(name = "success", location = "/WEB-INF/view/index.jsp") })
public class PortalAction extends AbsPortalAction
{
	protected final Logger log = Logger.getLogger(getClass());

	private String id;
	private Custom custom;
	@Inject
	private CustomService customService;

	public String execute()
	{
		custom = customService.get(getCurrentCustomId());
		log.debug(getCurrentCustomId());
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

	public Custom getCustom()
	{
		return custom;
	}

	public void setCustom(Custom custom)
	{
		this.custom = custom;
	}

}
