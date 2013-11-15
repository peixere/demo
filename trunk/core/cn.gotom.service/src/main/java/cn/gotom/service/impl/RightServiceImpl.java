package cn.gotom.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.beanutils.BeanUtils;

import cn.gotom.dao.jpa.GenericDaoJpa;
import cn.gotom.pojos.Right;
import cn.gotom.service.RightService;
import cn.gotom.service.model.RightChecked;
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
		List<Right> list = q.getResultList();
		return list;
	}

	@Override
	public List<Right> loadTree()
	{
		List<Right> list = findByParentId(null);
		for (Right r : list)
		{
			loadTreeCallback(r);
		}
		return list;
	}

	@Override
	public List<Right> loadTreeByParentId(String parentId)
	{
		List<Right> list = findByParentId(parentId);
		for (Right r : list)
		{
			loadTreeCallback(r);
		}
		return list;
	}

	private void loadTreeCallback(Right right)
	{
		List<Right> list = findByParentId(right.getId());
		right.setChildren(list);
		for (Right r : list)
		{
			loadTreeCallback(r);
		}
	}

	@Override
	public List<RightChecked> loadCheckedTree(List<Right> checkeds)
	{
		List<RightChecked> checkedList = new ArrayList<RightChecked>();
		List<Right> list = findByParentId(null);
		for (Right r : list)
		{
			RightChecked right = new RightChecked();
			try
			{
				BeanUtils.copyProperties(right, r);
			}
			catch (Exception e)
			{
				log.error("", e);
			}
			checkedList.add(right);
			loadCheckedTreeCallback(right, checkeds);
		}
		return checkedList;
	}

	private void loadCheckedTreeCallback(RightChecked p, List<Right> checkeds)
	{
		List<Right> list = findByParentId(p.getId());
		List<Right> checkedList = new ArrayList<Right>();
		p.setChildren(checkedList);
		for (Right r : list)
		{
			RightChecked right = new RightChecked();
			try
			{
				BeanUtils.copyProperties(right, r);
			}
			catch (Exception e)
			{
				log.error("", e);
			}
			checkedList.add(right);
			loadCheckedTreeCallback(right, checkeds);
		}
	}
}
