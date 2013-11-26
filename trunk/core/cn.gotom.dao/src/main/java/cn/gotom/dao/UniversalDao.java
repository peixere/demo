package cn.gotom.dao;

import java.util.List;

import cn.gotom.dao.jpa.UniversalDaoJpa;
import cn.gotom.service.Parameter;
import cn.gotom.service.UniversalService;

import com.google.inject.ImplementedBy;

@ImplementedBy(UniversalDaoJpa.class)
public interface UniversalDao extends UniversalService
{

	<T> List<T> find(Class<T> clazz, Parameter<?>... parameters);
	
	<T> T get(Class<T> clazz, Parameter<?>... parameters);
}
