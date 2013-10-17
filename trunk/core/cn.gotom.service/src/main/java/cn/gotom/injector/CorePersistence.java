package cn.gotom.injector;

import cn.gotom.dao.PersistenceLifeCycle;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;

@Singleton
public class CorePersistence implements PersistenceLifeCycle
{

	private final UnitOfWork unitOfWork;
	private final PersistService persistService;

	@Inject
	public CorePersistence(UnitOfWork unitOfWork, PersistService persistService)
	{
		this.unitOfWork = unitOfWork;
		this.persistService = persistService;
	}

	public void startService()
	{
		this.persistService.start();
	}

	public void stopService()
	{
		this.persistService.stop();
	}

	public void beginUnitOfWork()
	{
		this.unitOfWork.begin();
	}

	public void endUnitOfWork()
	{
		this.unitOfWork.end();
	}
}
