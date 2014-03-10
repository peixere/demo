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
import cn.gotom.pojos.Custom;
import cn.gotom.pojos.Right;
import cn.gotom.pojos.RightType;
import cn.gotom.pojos.User;
import cn.gotom.service.AuthenticationService;
import cn.gotom.service.CustomService;
import cn.gotom.service.UserService;
import cn.gotom.sso.client.AuthenticationFilter;
import cn.gotom.util.PasswordEncoder;
import cn.gotom.util.StringUtils;
import cn.gotom.vo.JsonResponse;
import cn.gotom.vo.MainInfo;

import com.google.inject.Inject;

@ParentPackage("json-default")
@Namespace(value = "/p")
@Action(value = "/main", results = { @Result(name = "success", location = "/WEB-INF/view/index.jsp") })
public class MainAction extends AbsPortalAction
{
	protected final Logger log = Logger.getLogger(getClass());
	@Inject
	protected UserService userService;
	@Inject
	protected CustomService customService;
	@Inject
	private AuthenticationService authService;
	@Inject
	private PasswordEncoder passwordEncoder;

	private String id;

	public String execute()
	{
		return "success";
	}

	public void main() throws IOException
	{
		MainInfo mainInfo = new MainInfo();
		mainInfo.setUsername(getCurrentUsername());
		User user = userService.getByUsername(mainInfo.getUsername());
		if (user != null)
		{
			mainInfo.setUserFullname(user.getName());
		}
		Custom custom = customService.get(getCurrentCustomId());
		mainInfo.setTitle(custom.getName());
		mainInfo.setLogoutUrl(AuthenticationFilter.getServerLogoutUrl());
		toJSON(mainInfo);
	}

	public void menu() throws IOException
	{
		String username = getCurrentUsername();
		List<Right> menuList = null;
		menuList = authService.findRightList(id, username, getCurrentCustomId());
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
		toJSON(menuList);
	}

	public void password()
	{
		String password = ServletActionContext.getRequest().getParameter("password");
		String newpass = ServletActionContext.getRequest().getParameter("newpass");
		String newpassCheck = ServletActionContext.getRequest().getParameter("newpassCheck");
		JsonResponse response = new JsonResponse();
		response.setSuccess(false);
		if (newpass != null && newpass.length() > 0 && newpass.equals(newpassCheck))
		{
			User old = userService.getByUsername(this.getCurrentUsername());
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
		toJSON(response);
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
