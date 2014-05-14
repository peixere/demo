package cn.gotom.dao.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.Table;

import cn.gotom.dao.GenericDao;
import cn.gotom.service.Parameter;
import cn.gotom.vo.Pagination;

import com.google.inject.persist.Transactional;

/**
 * CRUD
 * 
 * @author <a href="mailto:peixere@qq.com">裴绍国</a>
 * @param <T>
 *            a type variable
 * @param <PK>
 *            the primary key for that type
 */
public abstract class GenericDaoJpa<T, PK extends Serializable> extends UniversalDaoJpa implements GenericDao<T, PK>
{
	protected Class<T> persistentClass;

	protected String name;

	protected String table;

	public GenericDaoJpa(Class<T> persistentClass)
	{
		super();
		setPersistentClass(persistentClass);
	}

	private void setPersistentClass(Class<T> clazz)
	{
		this.persistentClass = clazz;
		this.name = persistentClass.getSimpleName();
		this.table = this.persistentClass.getAnnotation(Table.class).name();
	}

	public List<T> find(Parameter<?>... parameters)
	{
		String jpql = "select p from " + persistentClass.getSimpleName() + " p where 1 = 1";
		for (int i = 0; i < parameters.length; i++)
		{
			jpql += " and p." + parameters[i].getName() + " = :" + ArgsPrefix + i;
		}
		Query q = getEntityManager().createQuery(jpql);
		for (int i = 0; i < parameters.length; i++)
		{
			q.setParameter(ArgsPrefix + i, parameters[i].getValue());
		}
		@SuppressWarnings("unchecked")
		List<T> list = q.getResultList();
		return list;
	}

	@Override
	public T get(Parameter<?>... parameters)
	{
		return get(persistentClass, parameters);
	}

	@Override
	public T get(PK id)
	{
		try
		{
			return (T) getEntityManager().find(this.persistentClass, id);
		}
		catch (Exception ex)
		{
			log.warn(ex.getMessage() + " " + persistentClass.getName() + " " + id);
			return null;
		}
	}

	@Override
	public long getCount()
	{
		return getCount(this.persistentClass);
	}

	@Transactional
	@Override
	public void removeById(PK id)
	{
		T entity = getEntityManager().find(this.persistentClass, id);
		if (entity != null)
		{
			getEntityManager().remove(getEntityManager().getReference(persistentClass, id));
		}
	}

	@Transactional
	@Override
	public void removeAll()
	{
		String jpql = "delete from " + persistentClass.getSimpleName();
		getEntityManager().createQuery(jpql).executeUpdate();
	}

	@Transactional
	@Override
	public T save(T value)
	{
		return getEntityManager().merge(value);
	}

	@Override
	public List<T> findAll()
	{
		return find(-1, -1);
	}

	@Override
	public boolean exist(PK id)
	{
		T entity = (T) this.get(id);
		return entity != null;
	}

	@Override
	public T get(String name, Object value)
	{
		Parameter<Object> p = new Parameter<Object>(name, value);
		return get(persistentClass, p);
	}

	@Override
	public List<T> find(int maxResults, int firstResult)
	{
		return find(persistentClass, maxResults, firstResult);
	}

	@Override
	public Pagination<T> findPagination(int pageIndex, int pageSize)
	{
		return this.findPagination(persistentClass, pageIndex, pageSize);
	}
}
