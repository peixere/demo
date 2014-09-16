package cn.gotom.dao.jpa;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import cn.gotom.util.Converter;
import cn.gotom.vo.Pagination;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

abstract class AbsDaoJpa
{
	protected final Logger log = Logger.getLogger(getClass());

	protected final String ArgsPrefix = "arg";

	@Inject
	private Provider<EntityManager> provider;

	public AbsDaoJpa()
	{

	}

	protected EntityManager getEntityManager()
	{
		return provider.get();
	}

	protected Object getSingleResult(String jpql, Object... values)
	{
		Query q = getEntityManager().createQuery(jpql);
		if (values != null)
		{
			for (int i = 0; i < values.length; i++)
			{
				q.setParameter(i + 1, values[i]);
			}
		}
		q.setMaxResults(1);
		List<?> list = q.getResultList();
		if (!list.isEmpty())
		{
			return list.get(0);
		}
		else
		{
			return null;
		}
	}

	protected List<?> findList(String jpql, Object... values)
	{
		return findList(-1, jpql, values);
	}

	protected List<?> findList(int maxResults, String jpql, Object... values)
	{
		Query q = getEntityManager().createQuery(jpql);
		for (int i = 0; i < values.length; i++)
		{
			q.setParameter(i + 1, values[i]);
		}
		if (maxResults > 0)
		{
			q.setMaxResults(maxResults);
		}
		return q.getResultList();
	}

	protected List<?> findList(String jpql, Map<String, Object> params)
	{
		return findList(jpql, params, -1);
	}

	protected List<?> findList(String jpql, Map<String, Object> params, int maxResults)
	{
		Query q = getEntityManager().createQuery(jpql);
		for (String key : params.keySet())
		{
			q.setParameter(key, params.get(key));
		}
		if (maxResults > 0)
		{
			q.setMaxResults(maxResults);
		}
		return q.getResultList();
	}

	protected long count(String jpql, Object... values)
	{
		Query q = getEntityManager().createQuery(jpql);
		for (int i = 0; i < values.length; i++)
		{
			q.setParameter(i + 1, values[i]);
		}
		return (Long) q.getSingleResult();
	}

	protected long count(String jpql, Map<String, Object> params)
	{
		Query q = getEntityManager().createQuery(jpql);
		for (String key : params.keySet())
		{
			q.setParameter(key, params.get(key));
		}
		return (Long) q.getSingleResult();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Pagination<?> findPagination(String jpql, String countJpql, int pageIndex, int pageSize, Object... values)
	{
		if (pageIndex < 1)
		{
			pageIndex = 1;
		}
		Long count = count(countJpql, values);
		Query ql = getEntityManager().createQuery(jpql);
		for (int i = 0; i < values.length; i++)
		{
			ql.setParameter(i + 1, values[i]);
		}
		int first = (pageIndex - 1) * pageSize;
		ql.setFirstResult((first > 0 ? first : 0));
		ql.setMaxResults((pageSize > 1 ? pageSize : 20));
		return new Pagination(count.intValue(), ql.getResultList(), pageSize, pageIndex);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Pagination findPaginationByMap(String jpql, String countJpql, int pageIndex, int pageSize, Map<String, Object> params)
	{
		if (pageIndex < 1)
		{
			pageIndex = 1;
		}
		Long fullListSize = count(countJpql, params);
		Query q = getEntityManager().createQuery(jpql);
		for (String key : params.keySet())
		{
			Object value = params.get(key);
			q.setParameter(key, value);
		}
		int first = (pageIndex - 1) * pageSize;
		q.setFirstResult((first > 0 ? first : 0));
		q.setMaxResults((pageSize > 1 ? pageSize : 20));
		return new Pagination(fullListSize.intValue(), q.getResultList(), pageSize, pageIndex);
	}

	@Transactional
	protected void execute(String jpql, Object... values)
	{
		Query q = getEntityManager().createQuery(jpql);
		for (int i = 0; i < values.length; i++)
		{
			q.setParameter(i + 1, values[i]);
		}
		q.executeUpdate();
	}

	/**
	 * 在两个时期间
	 * 
	 * @param fieldname
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	protected String getBetweenDateToHql(String fieldname, Date startDate, Date endDate)
	{
		String begin = Converter.format(startDate, "yyyy-MM-dd 00:00:00");
		String end = Converter.format(endDate, "yyyy-MM-dd 23:59:59");
		String sql = fieldname + " between '" + begin + "' and '" + end + "'";
		return sql;
	}

	protected void nativeRemove(String table, String where, String value)
	{
		StringBuffer jpql = new StringBuffer();
		jpql.append("delete from " + table);
		jpql.append(" where " + where + " = :" + where);
		Query q = getEntityManager().createNativeQuery(jpql.toString());
		q.setParameter(where, value);
		q.executeUpdate();
	}

	protected void remove(String table, String where, String value)
	{
		StringBuffer jpql = new StringBuffer();
		jpql.append("delete from " + table);
		jpql.append(" where " + where + " = :" + where);
		Query q = getEntityManager().createQuery(jpql.toString());
		q.setParameter(where, value);
		q.executeUpdate();
	}
}
