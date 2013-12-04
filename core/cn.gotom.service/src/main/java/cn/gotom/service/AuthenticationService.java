package cn.gotom.service;

import java.util.List;

import cn.gotom.pojos.Right;

import com.google.inject.ImplementedBy;

@ImplementedBy(AuthenticationServiceImpl.class)
public interface AuthenticationService
{

	public abstract boolean validation(String username, String url);

	public abstract boolean validation(String username, String url, String appCode);

	/**
	 * 
	 * @param username
	 * @param parentId
	 * @return
	 */
	public abstract List<Right> findUserRightList(String username, String parentId);

	//public abstract List<Right> loadTreeByParentId(String username, String parentId);

}