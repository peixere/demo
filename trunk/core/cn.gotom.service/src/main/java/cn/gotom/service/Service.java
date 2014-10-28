package cn.gotom.service;

import java.util.List;

import cn.gotom.pojos.User;

import com.google.inject.ImplementedBy;

@ImplementedBy(ServiceImpl.class)
public interface Service extends InitializeService
{
	boolean saveUser(String currentUser, String customId, User user, String[] roleIds);

	// List<TreeCheckedModel> findSelectOrgs(String username, User user, String parentId);

	List<User> findUserByCustomId(String customId, String username);

	boolean userHasCustom(String userId, String customId);
}