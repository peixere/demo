package cn.gotom.service.impl;


import java.util.List;

import javax.persistence.Query;

import cn.gotom.dao.jpa.GenericDaoJpa;
import cn.gotom.pojos.Right;
import cn.gotom.service.RightService;
import cn.gotom.util.StringUtils;

import com.google.inject.Inject;

public class RightServiceImpl extends GenericDaoJpa<Right, String> implements RightService
{
	@Inject
	public RightServiceImpl()
	{
		super(Right.class);
	}

	@Override
	public List<Right> findByParentId(String parentId)
	{
		if (StringUtils.isNullOrEmpty(parentId))
		{
			parentId = "";
		}
		StringBuffer jpql = new StringBuffer();
		jpql.append("select p from " + persistentClass.getSimpleName() + " p");
		jpql.append(" where 1 = 1");
		jpql.append(" and p.parentId = :parentId");
		jpql.append(" order by sort");
		Query q = getEntityManager().createQuery(jpql.toString());
		q.setParameter("parentId", parentId);
		@SuppressWarnings("unchecked")
		List<Right> list = q.getResultList();
		return list;
	}

}
