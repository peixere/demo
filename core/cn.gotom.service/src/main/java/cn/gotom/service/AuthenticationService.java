package cn.gotom.service;

import java.util.List;

import cn.gotom.pojos.Custom;
import cn.gotom.pojos.Right;
import cn.gotom.pojos.User;

import com.google.inject.ImplementedBy;

@ImplementedBy(AuthenticationServiceImpl.class)
public interface AuthenticationService
{
	public abstract boolean validation(User user, String url);

	public abstract boolean validation(User user, String url, String appCode);

	/**
	 * 
	 * @param parentId
	 * @param username
	 * @param customId
	 * @return
	 */
	public abstract List<Right> findRightList(String parentId, String username, String customId);

	public abstract boolean isIgnore(String url);

	public abstract List<Custom> findCustomList(User user);

	public abstract Custom getDefaultCustom(User user);

}