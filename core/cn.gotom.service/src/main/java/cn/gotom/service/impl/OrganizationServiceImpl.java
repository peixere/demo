package cn.gotom.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import cn.gotom.dao.jpa.GenericDaoJpa;
import cn.gotom.pojos.Organization;
import cn.gotom.pojos.User;
import cn.gotom.service.OrganizationService;
import cn.gotom.util.StringUtils;

public class OrganizationServiceImpl extends GenericDaoJpa<Organization, String> implements OrganizationService
{
	public OrganizationServiceImpl()
	{
		super(Organization.class);
	}

	@Override
	public List<Organization> findByParentId(String parentId)
	{
		if (StringUtils.isNullOrEmpty(parentId))
		{
			parentId = "";
		}
		StringBuffer jpql = new StringBuffer();
		jpql.append("select p from " + persistentClass.getSimpleName() + " p");
		jpql.append(" where 1 = 1");
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
		if (!StringUtils.isNullOrEmpty(parentId))
		{
			q.setParameter("parentId", parentId);
		}
		@SuppressWarnings("unchecked")
		List<Organization> list = q.getResultList();
		return list;
	}

	@Override
	public List<Organization> findAllByParentId(String parentId)
	{
		List<Organization> list = new ArrayList<Organization>();
		findByParentIdCallback(list, parentId);
		return list;
	}

	private void findByParentIdCallback(List<Organization> orgList, String parentId)
	{
		List<Organization> list = findByParentId(parentId);
		orgList.addAll(list);
		for (Organization org : list)
		{
			findByParentIdCallback(orgList, org.getId());
		}
	}

	@Override
	public List<Organization> loadTree()
	{
		List<Organization> list = findByParentId(null);
		for (Organization r : list)
		{
			loadTreeCallback(r);
		}
		return list;
	}

	// @Override
	public List<Organization> loadTreeByParentId(String parentId)
	{
		List<Organization> list = findByParentId(parentId);
		for (Organization r : list)
		{
			loadTreeCallback(r);
		}
		return list;
	}

	private void loadTreeCallback(Organization p)
	{
		List<Organization> list = findByParentId(p.getId());
		p.setChildren(list);
		for (Organization r : list)
		{
			loadTreeCallback(r);
		}
	}

	@Override
	public Organization getByCode(String code)
	{
		StringBuffer jpql = new StringBuffer();
		jpql.append("select p from " + persistentClass.getSimpleName() + " p");
		jpql.append(" WHERE p.code=:code");
		Query q = getEntityManager().createQuery(jpql.toString());
		q.setParameter("code", code);
		Object obj = q.getSingleResult();
		return obj == null ? null : (Organization) obj;
	}

	@Override
	public List<Organization> findAllByUser(User user)
	{
		List<Organization> userOrgList = new ArrayList<Organization>();
		if (User.ROOT.equals(user.getUsername()))
		{
			userOrgList.addAll(this.findAll());
			return userOrgList;
		}
		else
		{
			if (user.getOrganizations() == null || user.getOrganizations().size() == 0)
			{
				user = this.get(User.class, user.getId());
			}
			userOrgList.addAll(user.getOrganizations());
			for (Organization org : user.getOrganizations())
			{
				findByParentIdCallback(userOrgList, org.getId());
			}
		}
		return userOrgList;
	}

	@Override
	public List<Organization> findSelectedByUser(User user)
	{
		List<Organization> userOrgList = null;
		if (User.ROOT.equals(user.getUsername()))
		{
			userOrgList = this.findAll();
		}
		else
		{
			userOrgList = user.getOrganizations();
			if (userOrgList == null || userOrgList.size() == 0)
			{
				User u = this.get(User.class, user.getId());
				userOrgList = u.getOrganizations();
			}
		}
		return userOrgList;
	}
}
