package cn.gotom.service.impl;

import java.util.List;

import javax.persistence.Query;

import cn.gotom.dao.jpa.GenericDaoJpa;
import cn.gotom.pojos.Organization;
import cn.gotom.pojos.User;
import cn.gotom.service.UserService;

import com.google.inject.Singleton;

@Singleton
public class UserServiceImpl extends GenericDaoJpa<User, String> implements UserService
{
	public UserServiceImpl()
	{
		super(User.class);
	}

	@Override
	public User getByUsername(String username)
	{
		User user = this.get("username", username);
		return user;
	}

	@Override
	public List<User> findAllByOrg(List<Organization> orgList)
	{
		String orgIds = "";
		if (orgList != null)
		{
			for (Organization o : orgList)
			{
				orgIds += "'" + o.getId() + "',";
			}
			if (orgIds.length() > 1)
			{
				orgIds = orgIds.substring(0, orgIds.length() - 1);
			}
		}
		String sql = " select user_id from core_org_user where org_id in(" + orgIds + ")";
		Object[] array = this.queryArray(sql);
		StringBuffer userIds = new StringBuffer();
		for (Object id : array)
		{
			userIds.append("'" + id + "',");
		}
		if (userIds.length() == 0)
			userIds.append("'',");
		StringBuffer jpql = new StringBuffer();
		jpql.append("select p from " + persistentClass.getSimpleName() + " p");
		jpql.append(" where p.id in (" + userIds.substring(0, userIds.length() - 1) + ")");
		Query q = getEntityManager().createQuery(jpql.toString());
		@SuppressWarnings("unchecked")
		List<User> list = q.getResultList();
		return list;
	}
}
