package cn.gotom.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.google.inject.persist.Transactional;

import cn.gotom.dao.jpa.GenericDaoJpa;
import cn.gotom.pojos.Custom;
import cn.gotom.pojos.Organization;
import cn.gotom.service.OrganizationService;
import cn.gotom.util.StringUtils;

public class OrganizationServiceImpl extends GenericDaoJpa<Organization, String> implements OrganizationService
{
	public OrganizationServiceImpl()
	{
		super(Organization.class);
	}

	@Override
	public boolean hasChildren(String parentId)
	{
		StringBuffer jpql = new StringBuffer();
		jpql.append("select p.id from " + persistentClass.getSimpleName() + " p");
		jpql.append(" where 1 = 1");
		if (!StringUtils.isNullOrEmpty(parentId))
		{
			jpql.append(" and p.parentId = :parentId");
		}
		else
		{
			jpql.append(" and (p.parentId IS NULL OR p.parentId = '' OR p.parentId = '0')");
		}
		Query q = getEntityManager().createQuery(jpql.toString());
		if (!StringUtils.isNullOrEmpty(parentId))
		{
			q.setParameter("parentId", parentId);
		}
		q.setMaxResults(1);
		return q.getResultList().size() > 0;
	}

	@Override
	public Organization getTop(String customId)
	{
		StringBuffer jpql = new StringBuffer();
		jpql.append("select p from " + persistentClass.getSimpleName() + " p");
		jpql.append(" where p.custom.id = :customId");
		jpql.append(" and (p.parentId IS NULL OR p.parentId = '' OR p.parentId = '0')");
		jpql.append(" order by sort");
		Query q = getEntityManager().createQuery(jpql.toString());
		q.setParameter("customId", customId);
		q.setMaxResults(1);
		@SuppressWarnings("unchecked")
		List<Organization> list = q.getResultList();
		if (list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<Organization> findByParentId(String customId, String parentId)
	{
		if (StringUtils.isNullOrEmpty(parentId))
		{
			parentId = "";
		}
		StringBuffer jpql = new StringBuffer();
		jpql.append("select p from " + persistentClass.getSimpleName() + " p");
		jpql.append(" where p.custom.id = :customId");
		if (!StringUtils.isNullOrEmpty(parentId))
		{
			jpql.append(" and p.parentId = :parentId");
		}
		else
		{
			jpql.append(" and (p.parentId IS NULL OR p.parentId = '' OR p.parentId = '0')");
		}
		jpql.append(" order by sort");
		Query q = getEntityManager().createQuery(jpql.toString());
		q.setParameter("customId", customId);
		if (!StringUtils.isNullOrEmpty(parentId))
		{
			q.setParameter("parentId", parentId);
		}
		@SuppressWarnings("unchecked")
		List<Organization> list = q.getResultList();
		return list;
	}

	@Override
	public List<Organization> findAllByParentId(String customId, String parentId)
	{
		List<Organization> list = new ArrayList<Organization>();
		findByParentIdCallback(customId, list, parentId);
		return list;
	}

	private void findByParentIdCallback(String customId, List<Organization> orgList, String parentId)
	{
		List<Organization> list = findByParentId(customId, parentId);
		orgList.addAll(list);
		for (Organization org : list)
		{
			findByParentIdCallback(customId, orgList, org.getId());
		}
	}

	@Override
	public List<Organization> loadTree(String customId)
	{
		return loadTreeByParentId(customId, null);
	}

	// @Override
	public List<Organization> loadTreeByParentId(String customId, String parentId)
	{
		List<Organization> list = findByParentId(customId, parentId);
		for (Organization r : list)
		{
			loadTreeCallback(customId, r);
		}
		return list;
	}

	private void loadTreeCallback(String customId, Organization p)
	{
		List<Organization> list = findByParentId(customId, p.getId());
		p.setChildren(list);
		for (Organization r : list)
		{
			loadTreeCallback(customId, r);
		}
	}

	@Override
	public Organization getByCode(String customId, String code)
	{
		StringBuffer jpql = new StringBuffer();
		jpql.append("select p from " + persistentClass.getSimpleName() + " p");
		jpql.append(" WHERE p.code=:code");
		jpql.append(" and p.custom.id=:customId");
		Query q = getEntityManager().createQuery(jpql.toString());
		q.setParameter("code", code);
		q.setParameter("customId", customId);
		Object obj = q.getSingleResult();
		return obj == null ? null : (Organization) obj;
	}

	//
	// @Override
	// public List<Organization> findAllByUser(User user)
	// {
	// List<Organization> userOrgList = new ArrayList<Organization>();
	// if (User.ROOT.equals(user.getUsername()))
	// {
	// userOrgList.addAll(this.findAll());
	// return userOrgList;
	// }
	// else
	// {
	// // if (user.getOrganizations() == null || user.getOrganizations().size() == 0)
	// // {
	// // user = this.get(User.class, user.getId());
	// // }
	// // userOrgList.addAll(user.getOrganizations());
	// // for (Organization org : user.getOrganizations())
	// // {
	// // findByParentIdCallback(userOrgList, org.getId());
	// // }
	// }
	// return userOrgList;
	// }
	//
	// @Override
	// public List<Organization> findSelectedByUser(User user)
	// {
	// List<Organization> userOrgList = new ArrayList<Organization>();
	// if (User.ROOT.equals(user.getUsername()))
	// {
	// userOrgList = this.findAll();
	// }
	// else
	// {
	// // userOrgList = user.getOrganizations();
	// // if (userOrgList == null || userOrgList.size() == 0)
	// // {
	// // User u = this.get(User.class, user.getId());
	// // userOrgList = u.getOrganizations();
	// // }
	// }
	// return userOrgList;
	// }
	//
	// @Override
	// public List<Organization> removeInUser(User user, List<Organization> oldOrgs)
	// {
	// List<Organization> orgList = findAllByUser(user);
	// for (Organization o : orgList)
	// {
	// for (Organization e : oldOrgs)
	// {
	// if (e.getId().equals(o.getId()))
	// {
	// oldOrgs.remove(e);
	// break;
	// }
	// }
	// }
	// return oldOrgs;
	// }
	@Transactional
	@Override
	public void updateEmpty(Custom custom)
	{
		StringBuffer jpql = new StringBuffer();
		jpql.append("update " + persistentClass.getSimpleName());
		jpql.append(" set custom_id = :customId where (custom_id IS NULL OR custom_id = '')");
		Query q = this.getEntityManager().createQuery(jpql.toString());
		q.setParameter("customId", custom.getId());
		q.executeUpdate();

	}
}
