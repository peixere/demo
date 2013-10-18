package cn.gotom.servlet;

import cn.gotom.injector.CoreAnnotation;
import cn.gotom.injector.CorePersistence;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GuicePersistMultiModulesFilter extends GuicePersistFilter
{

	@Inject
	public GuicePersistMultiModulesFilter(@CoreAnnotation CorePersistence manager)
	{
		super(manager);
		// this.manager = manager;
	}

}
