package cn.gotom.service;

import java.util.List;

import cn.gotom.pojos.Right;
import cn.gotom.pojos.User;

import com.google.inject.ImplementedBy;

@ImplementedBy(AuthenticationServiceImpl.class)
public interface AuthenticationService
{

	public abstract boolean validation(String username, String url);

	public abstract boolean validation(String username, String url, String appCode);

	public abstract boolean validation(User user, String url, String appCode);
	
	/**
	 * 
	 * @param username
	 * @param parentId
	 * @return
	 */
	public abstract List<Right> findRightList(String username, String parentId);

	

}