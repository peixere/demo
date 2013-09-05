package cn.gotom.dao;

import java.io.Serializable;
import java.util.List;

import cn.gotom.service.GenericService;
import cn.gotom.service.Parameter;

/**
 * GenericDao to CRUD POJOs
 * 
 * @author <a href="mailto:pqixere@qq.com">裴绍国</a>
 * @param <T>
 *            a type variable
 * @param <PK>
 *            the primary key for that type
 */
public interface GenericDao<T, PK extends Serializable> extends UniversalDao, GenericService<T, PK>
{
	List<T> find(Parameter<?>... parameters);

	T get(Parameter<?>... parameters);
}