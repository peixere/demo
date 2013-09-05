package cn.gotom.web.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;


@ParentPackage("json-default")
public class UserAction
{
	@Action(value = "/user", results = { @Result(name = "success", type = "json") })
	public String json()
	{
		return "success";
	}
}
