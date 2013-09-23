package cn.gotom.injector;

import cn.gotom.dao.PersistenceLifeCycle;
import cn.gotom.dao.jpa.UniversalDaoJpa;
import cn.gotom.service.CustomService;
import cn.gotom.service.DataInitializeService;
import cn.gotom.service.AuthService;
import cn.gotom.service.ResourceConfigService;
import cn.gotom.service.RightService;
import cn.gotom.service.RoleService;
import cn.gotom.service.impl.AuthServiceImpl;
import cn.gotom.service.impl.CustomServiceImpl;
import cn.gotom.service.impl.ResourceConfigServiceImpl;
import cn.gotom.service.impl.RightServiceImpl;
import cn.gotom.service.impl.RoleServiceImpl;
import cn.gotom.util.AntUrlPathMatcher;
import cn.gotom.util.UrlMatcher;

import com.google.inject.PrivateModule;
import com.google.inject.persist.jpa.JpaPersistModule;

/**
 * Private Persistent Module to connect with User unit.
 */
public class CorePersistModule extends PrivateModule
{

	@Override
	protected void configure()
	{
		install(new JpaPersistModule("AppEntityManager"));
		bind(PersistenceLifeCycle.class).annotatedWith(CoreAnnotation.class).to(CorePersistence.class);
		expose(PersistenceLifeCycle.class).annotatedWith(CoreAnnotation.class);
		bind(CorePersistService.class).asEagerSingleton();
		expose(CorePersistService.class);
		bind(UrlMatcher.class).to(AntUrlPathMatcher.class).asEagerSingleton();
		expose(UrlMatcher.class);	

		bind(RoleService.class).to(RoleServiceImpl.class).asEagerSingleton();
		expose(RoleService.class);
//		bind(UserService.class).to(UserServiceImpl.class).asEagerSingleton();
//		expose(UserService.class);
		bind(RightService.class).to(RightServiceImpl.class).asEagerSingleton();
		expose(RightService.class);
		bind(CustomService.class).to(CustomServiceImpl.class).asEagerSingleton();
		expose(CustomService.class);
		bind(AuthService.class).to(AuthServiceImpl.class).asEagerSingleton();
		expose(AuthService.class);
		bind(DataInitializeService.class).asEagerSingleton();
		expose(DataInitializeService.class);	
		
		bind(ResourceConfigService.class).to(ResourceConfigServiceImpl.class).asEagerSingleton();
		expose(ResourceConfigService.class);
		bind(UniversalDaoJpa.class).asEagerSingleton();
		expose(UniversalDaoJpa.class);
	}
}
