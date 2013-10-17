package cn.gotom.injector;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CorePersistService
{

	@Inject
	@CoreAnnotation
	CorePersistence manager;

	public void start()
	{
		manager.startService();
	}

	public void stop()
	{
		manager.stopService();
	}
}
