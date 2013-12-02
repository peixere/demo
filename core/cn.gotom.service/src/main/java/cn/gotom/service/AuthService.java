package cn.gotom.service;

import java.util.List;

import cn.gotom.pojos.Right;
import cn.gotom.service.impl.AuthServiceImpl;

import com.google.inject.ImplementedBy;

@ImplementedBy(AuthServiceImpl.class)
public interface AuthService
{

	public abstract boolean isAuth(String username, String url);

	public abstract boolean isAuth(String username, String url, String appCode);

	/**
	 * 
	 * @param username
	 * @param parentId
	 * @return
	 */
	public abstract List<Right> findRightList(String username, String parentId);

	public abstract List<Right> loadTreeByParentId(String parentId, String username);

}