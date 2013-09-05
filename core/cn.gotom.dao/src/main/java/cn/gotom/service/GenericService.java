package cn.gotom.service;

import java.io.Serializable;
import java.util.List;

/**
 * GenericDao to CRUD POJOs
 * 
 * @author <a href="mailto:qixere@qq.com">pei shaoguo</a>
 * @param <T>
 *            a type variable
 * @param <PK>
 *            the primary key for that type
 */
public interface GenericService<T, PK extends Serializable> extends UniversalService
{
	boolean exist(PK id);

	List<T> find(int maxResults, int firstResult);

	List<T> findAll();

	T get(String name, Object value);

	T get(PK id);

	long getCount();

	void remove(PK id);

	void removeAll();

	T save(T value);
}