package cn.gotom.web.action;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.service.AuthService;

import com.google.inject.Inject;

@ParentPackage("json-default")
public class AuthServiceAction extends JsonAction
{
	protected final Logger log = Logger.getLogger(getClass());

	@Inject
	private AuthService authService;

	@Action(value = "/authService", results = { @Result(name = "success", type = "json") })
	public void execute()
	{

	}
}
