package cn.gotom.servlet;

import cn.gotom.auth.server.AuthenticationService;
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
		bind(AuthenticationService.class).in(Singleton.class);
		filter("/authService").through(AuthenticationService.class);
	}

	protected void configureAuthenticatedServlets()
	{
		bind(AuthenticationFilter.class).in(Singleton.class);
		filter("/*").through(AuthenticationFilter.class);
	}

	protected void configureWebSocketServlets()
	{
		bind(WebSocket.class).in(Singleton.class);
		serve("/websocket.do").with(WebSocket.class);
	}
}
