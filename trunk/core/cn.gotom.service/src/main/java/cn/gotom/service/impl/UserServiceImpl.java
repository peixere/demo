package cn.gotom.service.impl;

import cn.gotom.dao.jpa.GenericDaoJpa;
import cn.gotom.pojos.User;
import cn.gotom.service.UserService;
import cn.gotom.util.PasswordEncoder;

import com.google.inject.Singleton;

@Singleton
public class UserServiceImpl extends GenericDaoJpa<User, String> implements UserService
{
	public UserServiceImpl()
	{
		super(User.class);
	}

	@Override
	public void init()
	{
		try
		{
			User user = getByUsername(User.admin);
			if (user == null)
			{
				user = new User();
				user.setUsername(User.admin);
				user.setName("超级管理员");
				PasswordEncoder passwordEncoder = new PasswordEncoder("MD5");
				user.setPassword(passwordEncoder.encode("a"));
				save(user);
			}
		}
		catch (Exception ex)
		{
			log.error("", ex);
		}
	}

	@Override
	public User getByUsername(String username)
	{
		User user = this.get("username", username);
		return user;
	}
}
