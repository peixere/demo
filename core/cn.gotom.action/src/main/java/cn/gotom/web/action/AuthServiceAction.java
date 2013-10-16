package cn.gotom.web.action;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.auth.client.AuthClient;
import cn.gotom.auth.client.Authenticated;
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
		try
		{
			String jsonString = AuthClient.convertStreamToString(ServletActionContext.getRequest().getInputStream());
			JSON json = JSONSerializer.toJSON(jsonString);
			log.debug(jsonString);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass(Authenticated.class);
			Authenticated authenticated = (Authenticated) JSONSerializer.toJava(json, jsonConfig);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
