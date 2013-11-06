package cn.gotom.dao.jpa;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import cn.gotom.util.Converter;
import cn.gotom.util.Pagination;
import cn.gotom.util.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Provider;

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
	protected Pagination<?> findPagination(String jpql, String countHql, int pageIndex, int pageSize, Object... values)
	{
		Long count = count(countHql, values);
		Query ql = getEntityManager().createQuery(jpql);
		for (int i = 0; i < values.length; i++)
		{
			ql.setParameter(i + 1, values[i]);
		}
		int first = pageIndex * pageSize;
		ql.setFirstResult((first > 0 ? first : 0));
		ql.setMaxResults((pageSize > 1 ? pageSize : 20));
		return new Pagination(count.intValue(), ql.getResultList(), pageSize, pageIndex);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Pagination findPaginationByMap(String jpql, String countHql, int pageIndex, int pageSize, Map<String, Object> params)
	{
		Long fullListSize = count(countHql, params);
		Query q = getEntityManager().createQuery(jpql);
		for (String key : params.keySet())
		{
			Object value = params.get(key);
			q.setParameter(key, value);
		}
		int first = pageIndex * pageSize;
		q.setFirstResult((first > 0 ? first : 0));
		q.setMaxResults((pageSize > 1 ? pageSize : 20));
		return new Pagination(fullListSize.intValue(), q.getResultList(), pageSize, pageIndex);
	}

	protected void execute(String jpql, Object... values)
	{
		Query q = getEntityManager().createQuery(jpql);
		for (int i = 0; i < values.length; i++)
		{
			q.setParameter(i + 1, values[i]);
		}
		q.executeUpdate();
	}

	protected String getBetweenDateToHql(String fieldname, Date startDate, Date endDate, String format)
	{
		if (StringUtils.isNullOrEmpty(format))
		{
			format = "yyyy-MM-dd";
		}
		String begin = Converter.format(startDate, format);
		String end = Converter.format(endDate, format);
		String sql = fieldname + " between to_date('" + begin + "', '" + format + "') and to_date('" + end + "', '" + format + "')";
		return sql;
	}
}
