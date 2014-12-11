package cn.gotom.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import cn.gotom.dao.jpa.UniversalDaoJpa;
import cn.gotom.vo.Pagination;

import com.google.inject.ImplementedBy;

/**
 * UniversalDao to CRUD POJOs
 * 
 * @author <a href="mailto:peixere@qq.com">裴绍国</a>
 */
@ImplementedBy(UniversalDaoJpa.class)
public interface UniversalService
{

	String getDefaultOrderBy(Class<?> clazz, String prefix);

	boolean exist(Class<?> clazz, Serializable id);

	<T> List<T> findByIds(Class<T> clazz, Serializable[] ids);

	<T> List<T> find(Class<T> clazz, int maxResults, int firstResult);

	<T> List<T> findAll(Class<T> clazz);

	<T> T get(Class<T> clazz, String name, Object value);

	<T> T get(Class<T> clazz, Serializable id);

	<T> T getLast(Class<T> clazz);

	long getCount(Class<?> clazz);

	void remove(Class<?> clazz, Serializable id);

	void removeById(Class<?> clazz, Serializable[] ids);

	void remove(Object value);

	void removeAll(Class<?> clazz);

	Object persist(Object value);

	void saveAll(Collection<?> entitys);

	void saveOrUpdateAll(Collection<?> entitys);

	<T> List<T> query(Class<T> clazz, String sql);

	int executeUpdate(String sql);

	<T> Pagination<T> findPagination(Class<T> clazz, int pageIndex, int pageSize);

}