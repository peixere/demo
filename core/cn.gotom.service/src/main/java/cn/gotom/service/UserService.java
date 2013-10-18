package cn.gotom.service;

import cn.gotom.pojos.User;
import cn.gotom.service.impl.UserServiceImpl;

import com.google.inject.ImplementedBy;

@ImplementedBy(UserServiceImpl.class)
public interface UserService extends GenericService<User, String>
{
	public void init();

	public User getByUsername(String username);
}
