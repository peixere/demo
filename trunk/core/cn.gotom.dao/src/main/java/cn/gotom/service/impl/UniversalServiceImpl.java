package cn.gotom.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import cn.gotom.dao.UniversalDao;
import cn.gotom.service.UniversalService;

import com.google.inject.Inject;

public class UniversalServiceImpl implements UniversalService
{
	protected final Logger log = Logger.getLogger(getClass());
	protected UniversalDao dao;

	@Inject
	public UniversalServiceImpl(UniversalDao dao)
	{
		this.dao = dao;
	}

	@Override
	public boolean exist(Class<?> clazz, Serializable id)
	{
		return dao.exist(clazz, id);
	}

	@Override
	public <T> List<T> find(Class<T> clazz, int maxResults, int firstResult)
	{
		return dao.find(clazz, maxResults, firstResult);
	}

	@Override
	public <T> List<T> findAll(Class<T> clazz)
	{
		return dao.findAll(clazz);
	}

	@Override
	public <T> T get(Class<T> clazz, String name, Object value)
	{
		return dao.get(clazz, name, value);
	}

	@Override
	public <T> T get(Class<T> clazz, Serializable id)
	{
		return dao.get(clazz, id);
	}

	@Override
	public long getCount(Class<?> clazz)
	{
		return dao.getCount(clazz);
	}

	@Override
	public void remove(Class<?> clazz, Serializable id)
	{
		dao.remove(clazz, id);
	}

	@Override
	public void remove(Object value)
	{
		dao.remove(value);
	}

	@Override
	public void removeAll(Class<?> clazz)
	{
		dao.removeAll(clazz);
	}

	@Override
	public Object persist(Object value)
	{
		return dao.persist(value);
	}

	@Override
	public void saveAll(Collection<?> entitys)
	{
		dao.saveAll(entitys);
	}

	@Override
	public <T> List<T> query(Class<T> clazz, String sql)
	{
		return dao.query(clazz, sql);
	}

	@Override
	public int executeUpdate(String sql)
	{
		return dao.executeUpdate(sql);
	}

	@Override
	public <T> List<T> findByIds(Class<T> clazz, Serializable[] ids)
	{
		return dao.findByIds(clazz,ids);
	}
}
