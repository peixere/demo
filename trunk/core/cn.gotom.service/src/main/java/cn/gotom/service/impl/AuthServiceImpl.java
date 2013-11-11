package cn.gotom.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cn.gotom.pojos.App;
import cn.gotom.pojos.ResourceConfig;
import cn.gotom.pojos.ResourceName;
import cn.gotom.pojos.Right;
import cn.gotom.pojos.Role;
import cn.gotom.pojos.User;
import cn.gotom.service.AuthService;
import cn.gotom.service.IUrlMatcher;
import cn.gotom.service.ResourceConfigService;
import cn.gotom.service.RightService;
import cn.gotom.service.RoleService;
import cn.gotom.service.UserService;
import cn.gotom.util.StringUtils;

import com.google.inject.Inject;

public class AuthServiceImpl implements AuthService
{
	protected final Logger log = Logger.getLogger(getClass());
	@Inject
	private UserService userService;

	@Inject
	private RoleService roleService;

	@Inject
	private RightService rightService;

	@Inject
	private IUrlMatcher urlMatcher;

	@Inject
	private ResourceConfigService resourceConfigService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.gotom.service.IAuthService#isAuth(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isAuth(String username, String url)
	{
		return isAuth(username, url, App.ROOT);
	}

	@Override
	public boolean isAuth(String username, String url, String appCode)
	{
		ResourceConfig rc = resourceConfigService.getByName(ResourceName.everyone_can_access);
		if (rc == null)
		{
			rc = new ResourceConfig();
			rc.setName(ResourceName.everyone_can_access);
			rc.setValue(Boolean.TRUE.toString());
			resourceConfigService.save(rc);
		}
		if (rc != null && Boolean.parseBoolean(rc.getValue()))
		{
			return true;
		}
		User user = userService.get("username", username);
		return isAuth(user, url, appCode);
	}

	private boolean isAuth(User user, String url, String appCode)
	{
		if (user == null)
		{
			return false;
		}
		else if (User.admin.equals(user.getUsername()))
		{
			return true;
		}
		if (StringUtils.isNullOrEmpty(appCode))
		{
			appCode = "";
		}
		if (user.getRoles() == null || user.getRoles().size() == 0)
		{
			user = userService.get(user.getId());
		}
		if (user.getRoles() != null)
		{
			for (Role role : user.getRoles())
			{
				if (role.getRights() == null || role.getRights().size() == 0)
				{
					role = roleService.get(role.getId());
				}
				if (role.getRights() != null)
				{
					for (Right right : role.getRights())
					{
						if (StringUtils.isNotEmpty(right.getResource()) && appCode.equals(right.getAppCode()))
						{
							String[] resource = right.getResource().trim().replace("；", ";").split(";");
							for (String pattern : resource)
							{
								if (urlMatcher.pathMatchesUrl(pattern, url))
								{
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.gotom.service.IAuthService#findRightList(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Right> findRightList(String username, String parentId)
	{
		List<Right> rightList = new ArrayList<Right>();
		User user = userService.getByUsername(username);
		if (user != null)
		{
			rightList = rightService.findByParentId(parentId);
			for (int i = rightList.size() - 1; i >= 0; i--)
			{
				boolean find = false;
				if (User.admin.equals(user.getUsername()))
				{
					find = true;
				}
				else
				{
					for (Role role : user.getRoles())
					{
						// role.getCompany()
						if (rightList.get(i).getRoles().contains(role))
						{
							find = true;
						}
					}
				}
				if (!find)
				{
					rightList.remove(i);
				}
			}
		}
		return rightList;
	}
}
