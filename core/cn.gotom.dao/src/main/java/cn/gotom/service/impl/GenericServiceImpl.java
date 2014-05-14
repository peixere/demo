package cn.gotom.service.impl;

import java.io.Serializable;
import java.util.List;

import cn.gotom.dao.GenericDao;
import cn.gotom.service.GenericService;
import cn.gotom.vo.Pagination;

public class GenericServiceImpl<T, PK extends Serializable> extends UniversalServiceImpl implements GenericService<T, PK>
{

	protected GenericDao<T, PK> dao;

	public GenericServiceImpl(final GenericDao<T, PK> genericDao)
	{
		super(genericDao);
		this.dao = genericDao;
	}

	@Override
	public boolean exist(PK id)
	{
		return dao.exist(id);
	}

	@Override
	public List<T> find(int maxResults, int firstResult)
	{
		return dao.find(maxResults, firstResult);
	}

	@Override
	public List<T> findAll()
	{
		return dao.findAll();
	}

	@Override
	public T get(String name, Object value)
	{
		return dao.get(name, value);
	}

	@Override
	public T get(PK id)
	{
		return dao.get(id);
	}

	@Override
	public long getCount()
	{
		return dao.getCount();
	}

	@Override
	public void removeById(PK id)
	{
		dao.remove(id);
	}

	@Override
	public void removeAll()
	{
		dao.removeAll();
	}

	@Override
	public T save(T value)
	{
		return dao.save(value);
	}

	@Override
	public Pagination<T> findPagination(int pageIndex, int pageSize)
	{
		return dao.findPagination(pageIndex, pageSize);
	}
}
