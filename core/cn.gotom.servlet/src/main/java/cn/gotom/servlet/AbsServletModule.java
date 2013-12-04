package cn.gotom.servlet;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;

import cn.gotom.sso.client.authentication.AuthenticationFilter;
import cn.gotom.web.util.filter.CharacterFilter;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

public abstract class AbsServletModule extends ServletModule
{
	@Override
	protected void configureServlets()
	{
		bind(CharacterFilter.class).in(Singleton.class);
		filter("/*").through(CharacterFilter.class);
		configureGuicePersistServlets();
		configureRemoteAuthServiceServlets();
		configureCasServlets();
		configureValidationFilter();
		configureStrutsServlets();
	}

	/**
	 * GuicePersist
	 */
	protected void configureGuicePersistServlets()
	{
		// filter("/*").through(GuicePersistMultiModulesFilter.class);//私有多多 Multiple Modules
		filter("/*").through(GuicePersistFilter.class);
	}

	protected abstract void configureRemoteAuthServiceServlets();

	/**
	 * 权限确认
	 */
	protected abstract void configureValidationFilter();

	/**
	 * Struts
	 */
	protected void configureStrutsServlets()
	{
		bind(StrutsPrepareAndExecuteFilter.class).in(Singleton.class);
		filter("/*").through(StrutsPrepareAndExecuteFilter.class);
	}

	/**
	 * 单元登录验证
	 */
	protected void configureCasServlets()
	{
		/*** cas ***/
		bind(AuthenticationFilter.class).in(Singleton.class);
		filter("/*").through(AuthenticationFilter.class);
		bind(Cas20ProxyReceivingTicketValidationFilter.class).in(Singleton.class);
		filter("/*").through(Cas20ProxyReceivingTicketValidationFilter.class);
		bind(HttpServletRequestWrapperFilter.class).in(Singleton.class);
		filter("/*").through(HttpServletRequestWrapperFilter.class);
		/*** cas ***/
	}
}
