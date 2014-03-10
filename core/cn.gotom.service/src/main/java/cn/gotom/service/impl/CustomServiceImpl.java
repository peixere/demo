package cn.gotom.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import cn.gotom.dao.jpa.GenericDaoJpa;
import cn.gotom.pojos.Custom;
import cn.gotom.pojos.CustomRight;
import cn.gotom.pojos.CustomUser;
import cn.gotom.pojos.Right;
import cn.gotom.pojos.User;
import cn.gotom.service.CustomService;

import com.google.inject.persist.Transactional;

public class CustomServiceImpl extends GenericDaoJpa<Custom, String> implements CustomService
{

	public CustomServiceImpl()
	{
		super(Custom.class);
	}

	@Override
	public List<User> findUserByCustomId(String customId)
	{
		StringBuffer jpql = new StringBuffer();
		jpql.append("Select p.user from " + CustomUser.class.getSimpleName() + " p");
		jpql.append(" where p.custom.id = :customId");
		Query q = getEntityManager().createQuery(jpql.toString());
		q.setParameter("customId", customId);
		@SuppressWarnings("unchecked")
		List<User> list = q.getResultList();
		return list;
	}

	@Transactional
	@Override
	public void saveAndRight(Custom custom, List<Right> rights)
	{
		List<String> params = new ArrayList<String>();
		params.add(custom.getId());
		removeCustomRight(params);
		this.getEntityManager().merge(custom);
		for (Right r : rights)
		{
			CustomRight cr = new CustomRight();
			cr.setCustom(custom);
			cr.setRight(r);
			this.getEntityManager().merge(cr);
		}
	}

	@Override
	public List<Right> findRights(String customId)
	{
		StringBuffer jpql = new StringBuffer();
		jpql.append("Select p.right from " + CustomRight.class.getSimpleName() + " p");
		jpql.append(" where p.custom.id = :customId");
		Query q = getEntityManager().createQuery(jpql.toString());
		q.setParameter("customId", customId);
		@SuppressWarnings("unchecked")
		List<Right> list = q.getResultList();
		return list;
	}

	@Transactional
	@Override
	public void removeByIds(String[] customIds)
	{
		List<String> params = new ArrayList<String>();
		for (String p : customIds)
		{
			params.add(p);
		}
		removeCustomRight(params);
		StringBuffer jpql = new StringBuffer();
		jpql.append("delete from " + Custom.class.getSimpleName() + " p");
		jpql.append(" where p.id in (:customId)");
		Query q = getEntityManager().createQuery(jpql.toString());
		q.setParameter("customId", params);
		q.executeUpdate();
	}

	private void removeCustomRight(List<String> customIds)
	{
		StringBuffer jpql = new StringBuffer();
		jpql.append("delete from " + CustomRight.class.getSimpleName() + " p");
		jpql.append(" where p.custom.id in (:customId)");
		Query q = getEntityManager().createQuery(jpql.toString());
		q.setParameter("customId", customIds);
		q.executeUpdate();
	}

	@Override
	public CustomUser getCustomUser(String userId, String customId)
	{
		StringBuffer jpql = new StringBuffer();
		jpql.append("Select p from " + CustomUser.class.getSimpleName() + " p");
		jpql.append(" where p.custom.id = :customId");
		jpql.append(" and p.user.id = :userId");
		Query q = getEntityManager().createQuery(jpql.toString());
		q.setParameter("customId", customId);
		q.setParameter("userId", userId);
		q.setMaxResults(1);
		@SuppressWarnings("unchecked")
		List<CustomUser> list = q.getResultList();
		if (list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

}
