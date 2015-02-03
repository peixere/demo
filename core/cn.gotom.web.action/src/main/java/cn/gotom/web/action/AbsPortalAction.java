package cn.gotom.web.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import cn.gotom.action.BaseAction;
import cn.gotom.pojos.Custom;
import cn.gotom.pojos.User;
import cn.gotom.util.StringUtils;

public abstract class AbsPortalAction extends BaseAction
{

	public User getCurrentUser()
	{
		return (User) ServletActionContext.getRequest().getAttribute(User.CurrentLoginUser);
	}

	public String getCurrentCustomId()
	{
		HttpServletRequest request = ServletActionContext.getRequest();
		String customId = (String) request.getSession().getAttribute(Custom.currentCustomId);
		if (StringUtils.isNullOrEmpty(customId))
		{
			customId = (String) request.getParameter("customId");
		}
		else
		{
			request.getSession().setAttribute(Custom.currentCustomId, customId);
		}
		if (StringUtils.isNullOrEmpty(customId))
		{
			User user = getCurrentUser();
			customId = user.getDefaultCustomId();
		}
		return customId;
	}

}
