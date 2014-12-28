package cn.gotom.servlet;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

import cn.gotom.servlet.GZIPFilter;
import cn.gotom.servlet.AuthenticationListener;
import cn.gotom.servlet.GuicePersistFilter;
import cn.gotom.sso.filter.CharacterFilter;
import cn.gotom.sso.server.ServerFilter;
import cn.gotom.sso.util.CommonUtils;
import cn.gotom.sso.websocket.WebSocketServer;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

/**
 * 
 * 
 * @author peixere@qq.com
 */
public class GuiceListener extends AuthenticationListener
{

	@Override
	protected ServletModule createServletModule()
	{
		ServletModule servletModule = new ServletModule()
		{
			@Override
			protected void configureServlets()
			{
				bind(WebSocketServer.class).in(Singleton.class);
				serve("/websocket.ws").with(WebSocketServer.class);

				bind(CharacterFilter.class).in(Singleton.class);
				filter("/*").through(CharacterFilter.class);

				bind(GZIPFilter.class).in(Singleton.class);
				filter("*.js").through(GZIPFilter.class);

				// filter("/*").through(GuicePersistMultiModulesFilter.class);//私有多多 Multiple Modules
				filter("/*").through(GuicePersistFilter.class);

				// bind(AuthenticationServiceFilter.class).in(Singleton.class);
				// filter("/authService").through(AuthenticationServiceFilter.class);
				if (!CommonUtils.isEmpty(serverLoginUrl))
				{
					bind(ServerFilter.class).in(Singleton.class);
					filter(serverLoginUrl).through(ServerFilter.class);
				}
				bind(ValidationFilter.class).in(Singleton.class);
				filter("/*").through(ValidationFilter.class);
				bind(StrutsPrepareAndExecuteFilter.class).in(Singleton.class);
				filter("*.do").through(StrutsPrepareAndExecuteFilter.class);
			}
		};
		return servletModule;
	}
}
