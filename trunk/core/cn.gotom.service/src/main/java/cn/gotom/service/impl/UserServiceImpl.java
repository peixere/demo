package cn.gotom.service.impl;

import java.util.List;

import cn.gotom.dao.jpa.GenericDaoJpa;
import cn.gotom.pojos.Organization;
import cn.gotom.pojos.User;
import cn.gotom.service.UserService;

import com.google.inject.Singleton;

@Singleton
public class UserServiceImpl extends GenericDaoJpa<User, String> implements UserService
{
	public UserServiceImpl()
	{
		super(User.class);
	}

	@Override
	public User getByUsername(String username)
	{
		User user = this.get("username", username);
		return user;
	}

	@Override
	public List<User> findAllByOrg(List<Organization> orgList)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
