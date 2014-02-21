package cn.gotom.service;

import java.util.List;

import cn.gotom.pojos.Organization;
import cn.gotom.pojos.User;
import cn.gotom.service.impl.OrganizationServiceImpl;

import com.google.inject.ImplementedBy;

@ImplementedBy(OrganizationServiceImpl.class)
public interface OrganizationService extends GenericService<Organization, String>
{
	/**
	 * 查询子集合
	 * 
	 * @param parentId
	 * @return
	 */
	List<Organization> findByParentId(String parentId);

	/**
	 * 查询所有后代集合
	 * 
	 * @param parentId
	 * @return
	 */
	List<Organization> findAllByParentId(String parentId);

	/**
	 * 加载树结构
	 * 
	 * @return
	 */
	List<Organization> loadTree();

	// List<Organization> loadTreeByParentId(String parentId);

	/**
	 * 根据用户查询所有后代集合
	 * 
	 * @param user
	 * @return
	 */
	List<Organization> findAllByUser(User user);

	Organization getByCode(String code);

	List<Organization> findSelectedByUser(User user);

	/**
	 * 删除用户可见的数据，
	 * @param login
	 * @param oldOrgs
	 * @return 用户不可见的数据
	 */
	List<Organization> removeInUser(User user, List<Organization> oldOrgs);
}
