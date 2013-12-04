package cn.gotom.injector;

import cn.gotom.dao.jpa.UniversalDaoJpa;
import cn.gotom.service.IAntUrlPathMatcher;
import cn.gotom.service.IUrlMatcher;
import cn.gotom.service.ResourceConfigService;
import cn.gotom.service.RightService;
import cn.gotom.service.RoleService;
import cn.gotom.service.UserService;
import cn.gotom.service.impl.ResourceConfigServiceImpl;
import cn.gotom.service.impl.RightServiceImpl;
import cn.gotom.service.impl.RoleServiceImpl;
import cn.gotom.service.impl.UserServiceImpl;

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
		//new JpaPersistModule("AppEntityManager")
		install(new JpaPersistModule("AppEntityManager"));
		bind(CorePersistence.class).annotatedWith(CoreAnnotation.class).to(CorePersistence.class);
		expose(CorePersistence.class).annotatedWith(CoreAnnotation.class);
		bind(CorePersistService.class).asEagerSingleton();
		expose(CorePersistService.class);
		bind(IUrlMatcher.class).to(IAntUrlPathMatcher.class).asEagerSingleton();
		expose(IUrlMatcher.class);

		bind(UniversalDaoJpa.class).asEagerSingleton();
		expose(UniversalDaoJpa.class);

		bind(RoleService.class).to(RoleServiceImpl.class).asEagerSingleton();
		expose(RoleService.class);
		bind(UserService.class).to(UserServiceImpl.class).asEagerSingleton();
		expose(UserService.class);
		bind(RightService.class).to(RightServiceImpl.class).asEagerSingleton();
		expose(RightService.class);
		bind(ResourceConfigService.class).to(ResourceConfigServiceImpl.class).asEagerSingleton();
		expose(ResourceConfigService.class);

	}
}
