package cn.gotom.service.impl;

import java.util.List;

import javax.persistence.Query;

import cn.gotom.dao.jpa.GenericDaoJpa;
import cn.gotom.pojos.Organization;
import cn.gotom.service.OrganizationService;
import cn.gotom.util.StringUtils;

import com.google.inject.Inject;

public class OrganizationServiceImpl extends GenericDaoJpa<Organization, String> implements OrganizationService
{
	@Inject
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
	public Organization findParentByCode(String code)
	{
		// SELECT * FROM core_organization p WHERE p.id IN
		// (SELECT parent_id FROM core_organization WHERE `code`='420100')
		// AND (p.parent_id IS NULL OR p.parent_id = '' OR p.parent_id = '0')
		StringBuffer jpql = new StringBuffer();
		jpql.append("select p from " + persistentClass.getSimpleName() + " p");
		jpql.append(" where 1 = 1");
		jpql.append(" and p.id IN (SELECT parentId FROM  " + persistentClass.getSimpleName() + " WHERE code=:code) ");
		jpql.append(" and (p.parentId IS NULL OR p.parentId = '' OR p.parentId = '0')");
		Query q = getEntityManager().createQuery(jpql.toString()); 
		q.setParameter("code", code);
		Object obj = q.getSingleResult();
		return obj == null ? null : (Organization) obj;
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

	@Override
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

}
