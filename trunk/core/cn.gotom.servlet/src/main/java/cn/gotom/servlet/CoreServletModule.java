package cn.gotom.servlet;

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
//		bind(AuthenticationServiceFilter.class).in(Singleton.class);
//		filter("/authService").through(AuthenticationServiceFilter.class);
	}

	protected void configureValidationFilter()
	{
		bind(ValidationFilter.class).in(Singleton.class);
		filter("/*").through(ValidationFilter.class);
	}

	protected void configureWebSocketServlets()
	{
		bind(WebSocket.class).in(Singleton.class);
		serve("/websocket.do").with(WebSocket.class);
	}
}
