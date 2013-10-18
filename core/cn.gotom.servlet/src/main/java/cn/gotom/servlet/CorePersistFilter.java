package cn.gotom.servlet;

import cn.gotom.injector.CoreAnnotation;
import cn.gotom.injector.CorePersistence;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CorePersistFilter extends GuicePersistFilter
{

	@Inject
	public CorePersistFilter(@CoreAnnotation CorePersistence manager)
	{
		this.manager = manager;
	}

}
