package cn.gotom.injector;

import cn.gotom.dao.Persistence;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;

@Singleton
public class CorePersistence extends Persistence
{
	@Inject
	public CorePersistence(UnitOfWork unitOfWork, PersistService persistService)
	{
		super(unitOfWork, persistService);
	}

}
