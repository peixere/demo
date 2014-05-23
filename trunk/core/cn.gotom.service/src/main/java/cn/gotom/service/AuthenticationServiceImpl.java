package cn.gotom.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cn.gotom.matcher.UrlMatcher;
import cn.gotom.pojos.App;
import cn.gotom.pojos.Custom;
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
	private UrlMatcher urlMatcher;

	@Inject
	private ResourceConfigService resourceConfigService;

	@Override
	public boolean validation(User user, String url)
	{
		return validation(user, url, App.ROOT);
	}

	@Override
	public boolean validation(User user, String url, String appCode)
	{
		if (without(url))
		{
			return true;
		}
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

	@Override
	public boolean isIgnore(String url)
	{
		return without(url);
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
			without.setValue("/p*.do;/p/main*.do;/*.html;/p/classes/**;/plugins/**");
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

	@Override
	public List<Right> findRightList(String parentId, String username, String customId)
	{
		List<Right> rightList = new ArrayList<Right>();
		User user = userService.getByUsername(username);
		if (user != null)
		{
			if (User.ROOT.equalsIgnoreCase(user.getUsername()))
			{
				rightList = rightService.findByParentId(parentId);
			}
			else
			{
				rightList = rightService.findByParentId(parentId, customId);
			}
			for (int i = rightList.size() - 1; i >= 0; i--)
			{
				boolean find = false;
				if (User.ROOT.equalsIgnoreCase(user.getUsername()))
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

	@Override
	public List<Custom> findCustomList(User user)
	{
		return userService.findCustomByUserIdList(user.getId());
	}

	@Override
	public Custom getDefaultCustom(User user)
	{
		List<Custom> list = findCustomList(user);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}
}
