package cn.gotom.service;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import cn.gotom.pojos.Custom;
import cn.gotom.pojos.CustomUser;
import cn.gotom.pojos.IdSerializable;
import cn.gotom.pojos.Organization;
import cn.gotom.pojos.Right;
import cn.gotom.pojos.Role;
import cn.gotom.pojos.RoleRight;
import cn.gotom.pojos.User;
import cn.gotom.util.PasswordEncoder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;

public class ServiceImpl implements Service
{
	protected final Logger log = Logger.getLogger(getClass());

	@Inject
	private UniversalService universalService;
	@Inject
	private UserService userService;
	@Inject
	private RightService rightService;
	@Inject
	private RoleService roleService;
	@Inject
	private PasswordEncoder passwordEncoder;
	@Inject
	private OrganizationService orgService;
	@Inject
	private CustomService customService;

	@Override
	public void init()
	{
		update();
		defaultCustom();
		defalutData();
		defaultCustomRight();
		initUser();
	}

	/**
	 * pojo更新要做的数据升级
	 */
	private void update()
	{
		RoleRight e = new RoleRight();
		String table = RoleRight.class.getAnnotation(Table.class).name();
		String sql = "";//"update " + table + " set version_nom = '" + e.getVersionNow() + "' where version_nom = '' or version_nom is null";
		//universalService.executeUpdate(sql);
		sql = "update " + table + " set version_create = '" + e.getVersionCreate() + "' where version_create = '' or version_create is null";
		universalService.executeUpdate(sql);
		sql = "update " + table + " set id = '" + RoleRight.randomID() + "' where id = '' or id is null";
		universalService.executeUpdate(sql);
	}

	private void initUser()
	{
		try
		{
			User user = userService.getByUsername(User.ROOT);
			if (user == null)
			{
				user = new User();
				user.setUsername(User.ROOT);
				user.setName("超级管理员");
				user.setPassword(passwordEncoder.encode("888888"));
				userService.save(user);
			}
			initAdmin();

		}
		catch (Exception ex)
		{
			log.error("", ex);
		}
	}

	private void initAdmin()
	{
		Custom custom = universalService.get(Custom.class, Custom.Default);
		Organization org = orgService.getTop(Custom.Default);
		if (org == null)
		{
			org = new Organization();
			org.setCode("000000");
			org.setCustom(custom);
			org.setSort(-10000);
			org.setText(custom.getName());
			org.setParentId("");
			orgService.save(org);
		}
		Role role = roleService.get(Custom.Default);
		if (role == null)
			role = roleService.get("name", "超级管理员");
		if (role == null)
		{
			role = new Role();
			role.setId(Custom.Default);
			role.setName("超级管理员");
			role.setOrganization(org);
		}
		roleService.save(role);
		List<Right> rights = roleService.findRight(role.getId());
		List<Right> rightList = rightService.findAll();
		for (Right right : rightList)
		{
			boolean find = false;
			for (Right r : rights)
			{
				if (right.getId().equals(r.getId()))
				{
					find = true;
					break;
				}
			}
			if (!find)
			{
				RoleRight rr = new RoleRight();
				rr.setRight(right);
				rr.setRole(role);
				roleService.persist(rr);
			}
		}
		User user = userService.getByUsername("admin");
		if (user == null)
		{
			user = new User();
			user.setUsername("admin");
			user.setName("管理员");
			user.setPassword(passwordEncoder.encode("1"));
			user.setRoles(new ArrayList<Role>());
		}
		if (!user.getRoles().contains(role))
			user.getRoles().add(role);
		userService.save(user);
		if (!userHasCustom(user.getId(), Custom.Default))
		{
			CustomUser cu = new CustomUser();
			cu.setCustom(custom);
			cu.setUser(user);
			universalService.persist(cu);
		}
	}

	private void defaultCustom()
	{
		try
		{
			Custom o = universalService.get(Custom.class, Custom.Default);
			if (o == null)
			{
				o = new Custom();
				o.setId(Custom.Default);
				o.setName("管理平台");
				universalService.persist(o);
			}
		}
		catch (Exception ex)
		{
			log.error("", ex);
		}
	}

	private void defalutData()
	{
		try
		{
			ResourceBundle bundle = ResourceBundle.getBundle("DefaultData", Locale.ROOT);
			Enumeration<String> keys = bundle.getKeys();
			while (keys.hasMoreElements())
			{
				String key = keys.nextElement();
				try
				{
					Class<?> clazz = Class.forName(key);
					if (clazz.equals(Right.class))
					{
						String json = bundle.getString(key);
						defalutDataSave(clazz, json);
					}
					else
					{
						List<?> olist = universalService.findAll(clazz);
						if (olist.size() == 0)
						{
							String json = bundle.getString(key);
							defalutDataSave(clazz, json);
							// JSON json = JSONSerializer.toJSON(rightString);
							// JsonConfig jsonConfig = new JsonConfig();
							// jsonConfig.setRootClass(clazz);
							// List<?> list = (List<?>) JSONSerializer.toJava(json, jsonConfig);
							// universalService.saveAll(olist);
						}
					}
				}
				catch (Exception ex)
				{
					log.error("初始化数据异常 " + key, ex);
				}
			}
			Custom custom = customService.get(Custom.Default);
			orgService.updateEmpty(custom);
			userService.updateEmpty(custom);
		}
		catch (Exception ex)
		{
			log.error("初始化数据异常", ex);
		}
	}

	private void defalutDataSave(Class<?> clazz, String json)
	{
		try
		{
			Gson gson = new Gson();
			@SuppressWarnings("unchecked")
			List<Map<String, ?>> list = (List<Map<String, ?>>) gson.fromJson(json, new TypeToken<List<?>>()
			{
			}.getType());
			if (list != null)
			{
				for (Map<String, ?> m : list)
				{
					IdSerializable o = (IdSerializable) clazz.newInstance();
					BeanUtils.populate(o, m);
					if (universalService.get(clazz, o.getId()) == null)
					{
						universalService.persist(o);
					}
				}
			}
		}
		catch (Exception ex)
		{
			log.error("初始化数据异常 " + clazz.getName(), ex);
		}
	}

	private void defaultCustomRight()
	{
		try
		{
			List<Right> rightList = customService.findRights(Custom.Default);
			if (rightList.size() == 0)
			{
				rightList = rightService.findAll();
				Custom custom = customService.get(Custom.Default);
				customService.saveAndRight(custom, rightList);
			}
		}
		catch (Exception ex)
		{
			log.error("", ex);
		}
	}

	@Override
	public boolean userHasCustom(String userId, String customId)
	{
		return customService.getCustomUser(userId, customId) != null;
	}

	@Override
	public boolean saveUser(String currentUser, String customId, User user, String[] roleIds)
	{
		CustomUser customUser = customService.getCustomUser(user.getId(), customId);
		User old = userService.getByUsername(user.getUsername());
		if (old != null && !old.getId().equals(user.getId()))
		{
			return false;
		}
		else
		{
			if (old != null)
			{
				user.setPassword(old.getPassword());
			}
			else
			{
				user.setPassword(passwordEncoder.encode("123456"));
			}
			List<Role> userRoles = new ArrayList<Role>();
			if (roleIds != null && roleIds.length > 0)
			{
				List<Role> roleList = roleService.findAll();
				for (Role o : roleList)
				{
					for (String roleId : roleIds)
					{
						if (o.getId().equals(roleId.trim()))
						{
							userRoles.add(o);
							break;
						}
					}
				}
			}
			user.setRoles(userRoles);
			userService.save(user);
			if (customUser == null)
			{
				customUser = new CustomUser();
				customUser.setUser(user);
				customUser.setCustom(new Custom());
				customUser.getCustom().setId(customId);
				userService.persist(customUser);
			}
		}
		return true;
	}

	//
	// @Override
	// public List<TreeCheckedModel> findSelectOrgs(String currentUser, User user, String parentId)
	// {
	// if (user != null)
	// {
	// user = userService.get(user.getId());
	// }
	// User login = userService.getByUsername(currentUser);
	// List<Organization> orgList = new ArrayList<Organization>();
	// if (User.ROOT.equals(login.getUsername()))
	// {
	// orgList = orgService.findByParentId(parentId);
	// }
	// else
	// {
	// // orgList = login.getOrganizations();
	// }
	// if (StringUtils.isNotEmpty(parentId))
	// {
	// orgList = orgService.findByParentId(parentId);
	// }
	// List<TreeCheckedModel> tree = new ArrayList<TreeCheckedModel>();
	// List<Organization> selectOrgs = new ArrayList<Organization>();
	// // if (user != null)
	// // {
	// // if (user.getOrganizations() == null)
	// // {
	// // user.setOrganizations(orgService.findSelectedByUser(user));
	// // }
	// // selectOrgs = user.getOrganizations();
	// // }
	// for (Organization o : orgList)
	// {
	// TreeCheckedModel e = new TreeCheckedModel();
	// for (Organization check : selectOrgs)
	// {
	// if (o.getId().equals(check.getId()))
	// {
	// e.setChecked(true);
	// break;
	// }
	// }
	// e.setId(o.getId());
	// e.setSort(o.getSort());
	// e.setText(o.getText());
	// tree.add(e);
	// }
	// return tree;
	// }

	@Override
	public List<User> findUserByCustomId(String customId, String username)
	{
		return customService.findUserByCustomId(customId, username);
	}

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub

	}
}
