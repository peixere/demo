package cn.gotom.web.action;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.pojos.App;
import cn.gotom.pojos.ResourceConfig;
import cn.gotom.pojos.ResourceName;
import cn.gotom.pojos.Right;
import cn.gotom.pojos.RightType;
import cn.gotom.pojos.User;
import cn.gotom.service.AuthenticationService;
import cn.gotom.service.ResourceConfigService;
import cn.gotom.service.UserService;
import cn.gotom.servlet.ResponseUtils;
import cn.gotom.util.PasswordEncoder;
import cn.gotom.util.StringUtils;
import cn.gotom.vo.JsonResponse;
import cn.gotom.vo.MainInfo;

import com.google.inject.Inject;

@ParentPackage("json-default")
@Namespace(value = "/p")
@Action(value = "/main", results = { @Result(name = "success", location = "/WEB-INF/view/index.jsp") })
public class MainAction
{
	protected final Logger log = Logger.getLogger(getClass());

	@Inject
	private AuthenticationService authService;
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
			menuList = authService.findUserRightList(username, id);
		}
		else
		{
			menuList = authService.findUserRightList(username, id);
			// menuList = authService.loadTreeByParentId(username, id);
		}
		String ctxp = ServletActionContext.getRequest().getContextPath();
		for (Right menu : menuList)
		{
			menu.setRoles(null);
			if (menu.getLeaf() && menu.getType().equalsIgnoreCase(RightType.URL))
			{
				if (StringUtils.isNullOrEmpty(menu.getAppCode()) || App.ROOT.equalsIgnoreCase(menu.getAppCode()))
				{
					menu.setComponent(ctxp + menu.getComponent());
				}
				else
				{
					menu.setComponent(menu.getAppCode() + menu.getComponent());
				}
			}
		}
		ResponseUtils.toJSON(menuList);
	}

	public void password()
	{
		PasswordEncoder passwordEncoder = new PasswordEncoder("MD5");
		String password = ServletActionContext.getRequest().getParameter("password");
		String newpass = ServletActionContext.getRequest().getParameter("newpass");
		String newpassCheck = ServletActionContext.getRequest().getParameter("newpassCheck");
		JsonResponse response = new JsonResponse();
		response.setSuccess(false);
		if (newpass != null && newpass.length() > 0 && newpass.equals(newpassCheck))
		{
			User old = userService.getByUsername(this.getUsername());
			if (old != null)
			{
				if (password != null && old.getPassword().equals(passwordEncoder.encode(password)))
				{
					old.setPassword(passwordEncoder.encode(newpass));
					userService.save(old);
					response.setSuccess(true);
				}
				else
				{
					response.setData("请输入正确的密码！");
				}
			}
			else
			{
				response.setData("找不到此用户！");
			}
		}
		else
		{
			response.setData("新密码和确认密码必须一样！");
		}
		ResponseUtils.toJSON(response);
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
