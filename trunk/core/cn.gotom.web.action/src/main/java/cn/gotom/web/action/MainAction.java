package cn.gotom.web.action;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.pojos.ResourceConfig;
import cn.gotom.pojos.ResourceName;
import cn.gotom.pojos.Right;
import cn.gotom.pojos.User;
import cn.gotom.service.AuthService;
import cn.gotom.service.ResourceConfigService;
import cn.gotom.service.UserService;
import cn.gotom.servlet.ResponseUtils;
import cn.gotom.util.StringUtils;
import cn.gotom.vo.MainInfo;

import com.google.inject.Inject;

@ParentPackage("json-default")
@Namespace(value = "/p")
@Action(value = "/main", results = { @Result(name = "success", location = "/WEB-INF/jsp/index.jsp") })
public class MainAction
{
	protected final Logger log = Logger.getLogger(getClass());

	@Inject
	private AuthService authService;
	@Inject
	private UserService userService;
	@Inject
	private ResourceConfigService configService;

	private String id;

	public String execute()
	{
		return "success";
	}

	public void main() throws IOException
	{
		MainInfo mainInfo = new MainInfo();
		mainInfo.setUsername(getUsername());
		User user = userService.getByUsername(mainInfo.getUsername());
		if (user != null)
		{
			mainInfo.setUserFullname(user.getName());
		}
		ResourceConfig appTitle = configService.getByName(ResourceName.appliction_title);
		if (appTitle == null)
		{
			appTitle = new ResourceConfig();
			appTitle.setName(ResourceName.appliction_title);
			appTitle.setValue("能源管理系统");
			configService.save(appTitle);
		}
		mainInfo.setTitle(appTitle.getValue());
		mainInfo.setLogoutUrl(ServletActionContext.getServletContext().getInitParameter("casServerLogoutUrl"));
		ResponseUtils.toJSON(mainInfo);
	}

	private String getUsername()
	{
		return ServletActionContext.getRequest().getRemoteUser();
	}

	public void menu() throws IOException
	{
		String username = getUsername();
		List<Right> menuList = null;
		if (StringUtils.isNullOrEmpty(id))
		{
			menuList = authService.findRightList(username, id);
		}
		else
		{
			menuList = authService.loadTreeByParentId(username, id);
		}
		ResponseUtils.toJSON(menuList);
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
