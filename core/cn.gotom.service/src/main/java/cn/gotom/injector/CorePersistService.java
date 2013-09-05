package cn.gotom.injector;

import cn.gotom.dao.PersistenceLifeCycle;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CorePersistService
{

	@Inject
	@CoreAnnotation
	PersistenceLifeCycle manager;

	public void start()
	{
		manager.startService();
	}

	public void stop()
	{
		manager.stopService();
	}
}
