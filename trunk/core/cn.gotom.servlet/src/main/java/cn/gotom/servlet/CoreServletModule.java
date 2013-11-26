package cn.gotom.servlet;

import cn.gotom.auth.server.AuthenticatedService;
import cn.gotom.websocket.WebSocket;

import com.google.inject.Singleton;

public class CoreServletModule extends AbsServletModule
{
	@Override
	protected void configureServlets()
	{
		super.configureServlets();
		configureWebSocketServlets();
	}

	protected void configureRemoteAuthServiceServlets()
	{
		bind(AuthenticatedService.class).in(Singleton.class);
		filter("/authService").through(AuthenticatedService.class);
	}

	protected void configureAuthenticatedServlets()
	{
		filter("/*").through(AuthenticatedFilter.class);
	}

	protected void configureWebSocketServlets()
	{
		bind(WebSocket.class).in(Singleton.class);
		serve("/websocket.do").with(WebSocket.class);
	}
}
