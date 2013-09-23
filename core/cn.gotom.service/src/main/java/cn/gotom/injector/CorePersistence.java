package cn.gotom.injector;

import javax.persistence.EntityManager;

import cn.gotom.dao.PersistenceLifeCycle;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;

@Singleton
class CorePersistence implements PersistenceLifeCycle
{

	private final UnitOfWork unitOfWork;
	private final PersistService persistService;
	private Provider<EntityManager> provider;

	@Inject
	public CorePersistence(UnitOfWork unitOfWork, PersistService persistService, Provider<EntityManager> provider)
	{
		this.unitOfWork = unitOfWork;
		this.persistService = persistService;
		this.provider = provider;
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

	public EntityManager get()
	{
		return this.provider.get();
	}
}
