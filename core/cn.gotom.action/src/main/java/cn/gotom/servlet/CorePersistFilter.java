package cn.gotom.servlet;

import cn.gotom.dao.PersistenceLifeCycle;
import cn.gotom.injector.CoreAnnotation;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CorePersistFilter extends GuicePersistFilter
{

	@Inject
	public CorePersistFilter(@CoreAnnotation PersistenceLifeCycle manager)
	{
		this.manager = manager;
	}

}
