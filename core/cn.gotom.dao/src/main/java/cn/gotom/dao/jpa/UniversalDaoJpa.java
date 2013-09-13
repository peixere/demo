package cn.gotom.dao.jpa;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Query;

import org.hibernate.transform.Transformers;

import cn.gotom.dao.JdbcUtils;
import cn.gotom.dao.UniversalDao;
import cn.gotom.service.Parameter;

import com.google.inject.persist.Transactional;

/**
 * CRUD
 * 
 * @author <a href="mailto:bwnoll@qq.com">裴绍国</a>
 * @param <T>
 *            a type variable
 * @param <PK>
 *            the primary key for that type
 */
public class UniversalDaoJpa extends AbsDaoJpa implements UniversalDao
{

	public UniversalDaoJpa()
	{
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> find(Class<T> clazz, int maxResults, int firstResult)
	{
		String jpql = "select p from " + clazz.getSimpleName() + " p";
		String orderby = " order by ";
		Field sort = findField(clazz, "sort");
		if (sort != null && sort.isAnnotationPresent(Column.class))
		{
			orderby = orderby + " p." + sort.getName() + " asc,";
		}
		Field versionNum = findField(clazz, "versionNum");
		if (versionNum != null && versionNum.isAnnotationPresent(Column.class))
		{
			orderby = orderby + " p." + versionNum.getName() + " desc,";
		}
		if (orderby.endsWith(","))
		{
			jpql += orderby.substring(0, orderby.length() - 1);
		}
		Query q = getEntityManager().createQuery(jpql);
		if (maxResults > 0)
		{
			q.setMaxResults(maxResults);
		}
		if (maxResults > 0 && firstResult >= 0)
		{
			q.setFirstResult(firstResult);
		}
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> clazz, Parameter<?>... parameters)
	{
		String jpql = "select p from " + clazz.getSimpleName() + " p where 1 = 1";
		for (int i = 0; i < parameters.length; i++)
		{
			jpql += " and p." + parameters[i].getName() + " = :" + ArgsPrefix + i;
		}
		Query q = getEntityManager().createQuery(jpql);
		for (int i = 0; i < parameters.length; i++)
		{
			q.setParameter(ArgsPrefix + i, parameters[i].getValue());
		}
		q.setMaxResults(1);
		List<?> list = q.getResultList();
		if (!list.isEmpty())
		{
			return (T) list.get(0);
		}
		else
		{
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> find(Class<T> clazz, Parameter<?>... parameters)
	{
		String jpql = "select p from " + clazz.getSimpleName() + " p where 1 = 1";
		for (int i = 0; i < parameters.length; i++)
		{
			jpql += " and p." + parameters[i].getName() + " = " + ArgsPrefix + i;
		}
		Query q = getEntityManager().createQuery(jpql);
		for (int i = 0; i < parameters.length; i++)
		{
			q.setParameter(ArgsPrefix + i, parameters[i].getValue());
		}
		List<T> list = q.getResultList();
		return list;
	}

	@Override
	public <T> T get(Class<T> clazz, Serializable id)
	{
		return getEntityManager().find(clazz, id);
	}

	@Override
	public long getCount(Class<?> clazz)
	{
		Query q = getEntityManager().createQuery("select count(p) from " + clazz.getSimpleName() + " p");
		return ((Long) q.getSingleResult()).longValue();
	}

	@Transactional
	@Override
	public void remove(Object value)
	{
		getEntityManager().remove(getEntityManager().merge(value));
	}

	@Transactional
	@Override
	public void remove(Class<?> clazz, Serializable id)
	{
		getEntityManager().remove(getEntityManager().find(clazz, id));
	}

	@Transactional
	@Override
	public void removeAll(Class<?> clazz)
	{
		String jpql = "delete from " + clazz.getSimpleName();
		getEntityManager().createQuery(jpql).executeUpdate();
	}

	@Transactional
	@Override
	public Object persist(Object value)
	{
		return getEntityManager().merge(value);
	}

	@Transactional
	@Override
	public void saveAll(Collection<?> entitys)
	{
		for (Object v : entitys)
		{
			getEntityManager().merge(v);
		}
	}

	@Override
	public boolean exist(Class<?> clazz, Serializable id)
	{
		Object entity = this.get(clazz, id);
		return entity != null;
	}

	@Override
	public <T> T get(Class<T> clazz, String name, Object value)
	{
		Parameter<Object> p = new Parameter<Object>(name, value);
		return get(clazz, p);
	}

	@Override
	public <T> List<T> findAll(Class<T> clazz)
	{
		return find(clazz, -1, -1);
	}

	protected Field findField(Class<?> clazz, String name)
	{
		Field field = null;
		Class<?> superclass = clazz;
		while (field == null && !superclass.equals(Object.class))
		{
			try
			{
				field = superclass.getDeclaredField(name);
			}
			catch (Exception e)
			{
				;
			}
			superclass = superclass.getSuperclass();
		}
		return field;
	}

	@Override
	public <T> List<T> query(Class<T> clazz, String sql)
	{
		List<T> eList = null;
		try
		{
			eList = JdbcUtils.toList(clazz, query(sql));
		}
		catch (SQLException ex)
		{
			log.error("初始化连接错误", ex);
			eList = new ArrayList<T>();
		}
		return eList;
	}

	@Override
	public int executeUpdate(String sql)
	{
		return this.getEntityManager().createNativeQuery(sql).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	protected List<Map<String, Object>> query(String sql)
	{
		Query query = this.getEntityManager().createNativeQuery(sql);
		query.unwrap(org.hibernate.SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.getResultList();
	}
}
