package cn.gotom.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cn.gotom.pojos.App;
import cn.gotom.pojos.ResourceConfig;
import cn.gotom.pojos.ResourceName;
import cn.gotom.pojos.Right;
import cn.gotom.pojos.Role;
import cn.gotom.pojos.User;
import cn.gotom.util.StringUtils;

import com.google.inject.Inject;

public class AuthenticationServiceImpl implements AuthenticationService
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
	public boolean validation(String username, String url)
	{
		return validation(username, url, App.ROOT);
	}

	@Override
	public boolean validation(String username, String url, String appCode)
	{
		if (without(url))
		{
			return true;
		}
		User user = userService.get("username", username);
		return isAuth(user, url, appCode);
	}

	private boolean without(String url)
	{
		ResourceConfig without = resourceConfigService.getByName(ResourceName.validation_without);
		if (without == null)
		{
			without = new ResourceConfig();
			without.setName(ResourceName.validation_without);
			without.setValue(Boolean.FALSE.toString());
			resourceConfigService.save(without);
		}
		if (without != null && Boolean.parseBoolean(without.getValue()))
		{
			return true;
		}
		without = resourceConfigService.getByName(ResourceName.validation_without_path);
		if (without == null)
		{
			without = new ResourceConfig();
			without.setName(ResourceName.validation_without_path);
			without.setValue("/p.do;/p/main*.do");
			resourceConfigService.save(without);
		}
		String none = without.getValue();
		none = none.trim().replace("；", ";");
		none = none.replace(",", ";");
		none = none.replace("，", ";");
		none = none.replace("\n", ";");
		String[] resource = none.split(";");
		for (String pattern : resource)
		{
			if (urlMatcher.pathMatchesUrl(pattern, url))
			{
				return true;
			}
		}
		return false;
	}

	private boolean isAuth(User user, String url, String appCode)
	{
		if (user == null)
		{
			return false;
		}
		else if (User.ROOT.equals(user.getUsername()))
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
	 * @see cn.gotom.service.IAuthService#findUserRightList(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Right> findUserRightList(String username, String parentId)
	{
		List<Right> rightList = new ArrayList<Right>();
		User user = userService.getByUsername(username);
		if (user != null)
		{
			rightList = rightService.findByParentId(parentId);
			for (int i = rightList.size() - 1; i >= 0; i--)
			{
				boolean find = false;
				if (User.ROOT.equals(user.getUsername()))
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

	// @Override
	public List<Right> loadTreeByParentId(String username, String parentId)
	{
		// User user = userService.getByUsername(username);
		List<Right> rightList = rightService.findByParentId(parentId);
		// if (user != null)
		// {
		// for (int i = rightList.size() - 1; i >= 0; i--)
		// {
		// boolean find = false;
		// if (User.admin.equals(user.getUsername()))
		// {
		// find = true;
		// }
		// else
		// {
		// for (Role role : user.getRoles())
		// {
		// // role.getCompany()
		// if (rightList.get(i).getRoles().contains(role))
		// {
		// find = true;
		// }
		// }
		// }
		// if (!find)
		// {
		// rightList.remove(i);
		// }
		// }
		// }
		// for (Right r : rightList)
		// {
		// loadTreeCallback(r, user);
		// }
		return rightList;
	}

	// private void loadTreeCallback(Right right, User user)
	// {
	// List<Right> rightList = rightService.findByParentId(right.getId());
	// if (user != null)
	// {
	// for (int i = rightList.size() - 1; i >= 0; i--)
	// {
	// boolean find = false;
	// if (User.admin.equals(user.getUsername()))
	// {
	// find = true;
	// }
	// else
	// {
	// for (Role role : user.getRoles())
	// {
	// if (rightList.get(i).getRoles().contains(role))
	// {
	// find = true;
	// }
	// }
	// }
	// if (!find)
	// {
	// rightList.remove(i);
	// }
	// }
	// }
	// right.setChildren(rightList);
	// for (Right r : rightList)
	// {
	// loadTreeCallback(r, user);
	// }
	// }

}
