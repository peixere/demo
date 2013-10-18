package cn.gotom.servlet;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;

import cn.gotom.auth.server.AuthenticatedService;
import cn.gotom.servlet.websocket.WebSocket;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

public class CoreServletModule extends ServletModule
{
	@Override
	protected void configureServlets()
	{
		//filter("/*").through(GuicePersistMultiModulesFilter.class);//私有多多 Multiple Modules
		filter("/*").through(GuicePersistFilter.class);
		bind(AuthenticatedService.class).in(Singleton.class);
		filter("/authService").through(AuthenticatedService.class);

		/*** cas ***/
		bind(org.jasig.cas.client.authentication.AuthenticationFilter.class).in(Singleton.class);
		filter("/*").through(org.jasig.cas.client.authentication.AuthenticationFilter.class);
		bind(Cas20ProxyReceivingTicketValidationFilter.class).in(Singleton.class);
		filter("/*").through(Cas20ProxyReceivingTicketValidationFilter.class);
		bind(HttpServletRequestWrapperFilter.class).in(Singleton.class);
		filter("/*").through(HttpServletRequestWrapperFilter.class);
		/*** cas ***/

		filter("/*").through(AuthenticatedFilter.class);
		bind(StrutsPrepareAndExecuteFilter.class).in(Singleton.class);
		filter("/*").through(StrutsPrepareAndExecuteFilter.class);
		bind(WebSocket.class).in(Singleton.class);
		serve("/websocket.do").with(WebSocket.class);
	}
}
