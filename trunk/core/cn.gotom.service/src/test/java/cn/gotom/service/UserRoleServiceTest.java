package cn.gotom.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Logger;

import cn.gotom.dao.JdbcUtils;
import cn.gotom.dao.Persistence;
import cn.gotom.dao.PersistenceLifeCycle;
import cn.gotom.pojos.Role;
import cn.gotom.pojos.Status;
import cn.gotom.pojos.User;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;

@SuppressWarnings("deprecation")
public class UserRoleServiceTest extends TestCase
{

	protected final Logger log = Logger.getLogger(getClass());
	private Injector injector;

	@Override
	public void setUp()
	{
		try
		{
			System.out.println("------------------- setUp -----------------------");
			userList();
			roleList();
			JpaPersistModule jpm = new JpaPersistModule("AppEntityManager");
			Properties properties = new Properties();
			properties.load(this.getClass().getResourceAsStream("/jdbc.properties"));
			jpm.properties(properties);
			injector = Guice.createInjector(jpm,new ServiceModule());
			injector.getInstance(PersistenceLifeCycle.class).startService();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@Override
	public void tearDown()
	{
		injector.getInstance(Persistence.class).stopService();
		JdbcUtils.closeCurrent();
		System.out.println("------------------- tearDown -----------------------");
	}

	private List<User> userList;

	private List<User> userList()
	{
		if (userList == null)
		{
			userList = new ArrayList<User>();
			int count = 5;
			for (int i = 0; i < count; i++)
			{
				User user = new User();
				user.setUsername("user" + i);
				user.setPassword("admin");
				user.setName("管理员" + i);
				user.setStatus(i % 2 == 0 ? Status.Normal : Status.Delete);
				userList.add(user);
			}
		}
		return userList;
	}

	private List<Role> roleList;

	private List<Role> roleList()
	{
		if (roleList == null)
		{
			roleList = new ArrayList<Role>();
			int count = 5;
			for (int i = 0; i < count; i++)
			{
				Role role = new Role();
				role.setName("权限" + i);
				role.setSort(i);
				roleList.add(role);
			}
		}
		return roleList;
	}

	@SuppressWarnings("unused")
	public void testSaveUserRole()
	{
		AuthenticationService authService = injector.getInstance(AuthenticationService.class);
		Assert.assertNotNull(authService);
		UserService userDao = injector.getInstance(UserService.class);
		String sql = "select  t.id, t.text, t.component, " + " t.description, t.type, t.iconCls, t.sort " + " from resource t where t.parent_id is null";
		
		userDao.removeAll();
		for (User user : userList)
		{
			userDao.save(user);
		}
		Assert.assertTrue(userDao.findAll().size() == userList.size());
		RoleService roleDao = injector.getInstance(RoleService.class);
		roleDao.removeAll();
		for (Role r : roleList)
		{
			roleDao.save(r);
		}
		Assert.assertTrue(roleDao.findAll().size() == roleList.size());
		List<User> list = userDao.findAll();
		for (User user : list)
		{
			User old = userDao.get(user.getId());
			if (old.getRoles() == null)
			{
				old.setRoles(new ArrayList<Role>());
			}
			for (Role r : roleList)
			{
				if (!old.getRoles().contains(r))
				{
					old.getRoles().add(r);
				}
			}
			userDao.save(old);
		}
		List<User> rlist = userDao.findAll();
		int i = rlist.size();
		for (User r : rlist)
		{
			if (i % 2 == 0)
			{
				userDao.remove(r);
			}
			else
			{
				userDao.remove(r.getId());
			}
		}
		Assert.assertTrue(userDao.findAll().size() == 0);
		Assert.assertTrue(roleDao.findAll().size() == roleList.size());
	}
}
