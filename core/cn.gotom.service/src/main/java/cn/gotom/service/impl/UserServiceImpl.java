package cn.gotom.service.impl;

import java.util.List;

import javax.persistence.Query;

import cn.gotom.dao.jpa.GenericDaoJpa;
import cn.gotom.pojos.Custom;
import cn.gotom.pojos.CustomUser;
import cn.gotom.pojos.Organization;
import cn.gotom.pojos.UploadFile;
import cn.gotom.pojos.User;
import cn.gotom.service.UserService;
import cn.gotom.util.StringUtils;

import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

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
		if (user == null)
			user = this.get("mobile", username);
		return user;
	}

	@Override
	public User getByMobile(String mobile)
	{
		User user = this.get("mobile", mobile);
		if (user == null)
			this.get("username", mobile);
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
		StringBuffer userIds = new StringBuffer();
		if (StringUtils.isNotEmpty(orgIds))
		{
			String sql = "select user_id from core_org_user where org_id in(" + orgIds + ")";
			Object[] array = this.queryArray(sql);
			for (Object id : array)
			{
				userIds.append("'" + id + "',");
			}
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

	@Transactional
	@Override
	public void updateEmpty(Custom custom)
	{
		StringBuffer jpql = new StringBuffer();
		jpql.append("update " + persistentClass.getSimpleName());
		jpql.append(" set defaultCustomId = :customId where (defaultCustomId IS NULL OR defaultCustomId = '')");
		Query q = this.getEntityManager().createQuery(jpql.toString());
		q.setParameter("customId", custom.getId());
		q.executeUpdate();

	}

	@Override
	public List<Custom> findCustomByUserIdList(String userId)
	{
		StringBuffer jpql = new StringBuffer();
		jpql.append("select p.custom from " + CustomUser.class.getSimpleName() + " p");
		jpql.append(" where p.user.id = :userId");
		Query q = getEntityManager().createQuery(jpql.toString());
		q.setParameter("userId", userId);
		@SuppressWarnings("unchecked")
		List<Custom> list = q.getResultList();
		return list;
	}

	@Transactional
	@Override
	public void delete(User user)
	{
		remove(UploadFile.class.getSimpleName(), "user_id", user.getId());
		remove(CustomUser.class.getSimpleName(), "user_id", user.getId());
		getEntityManager().remove(getEntityManager().getReference(persistentClass, user.getId()));
	}
}
