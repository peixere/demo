package cn.gotom.web.action;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

@ParentPackage("json-default")
@Namespace(value = "")
@Action(value = "/p", results = { @Result(name = "success", location = "/WEB-INF/view/index.jsp") })
public class DefaultAction
{
	protected final Logger log = Logger.getLogger(getClass());

	private String id;

	public String execute()
	{
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

}
