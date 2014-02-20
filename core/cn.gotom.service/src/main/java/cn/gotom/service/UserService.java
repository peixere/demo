package cn.gotom.service;

import java.util.List;

import cn.gotom.pojos.Organization;
import cn.gotom.pojos.User;
import cn.gotom.service.impl.UserServiceImpl;

import com.google.inject.ImplementedBy;

@ImplementedBy(UserServiceImpl.class)
public interface UserService extends GenericService<User, String>
{
	public User getByUsername(String username);

	public List<User> findAllByOrg(List<Organization> orgList);
}
