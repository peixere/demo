package cn.gotom.servlet;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

import cn.gotom.servlet.websocket.WebSocket;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

public class CoreServletModule extends ServletModule
{
	@Override
	protected void configureServlets()
	{
		filter("/*").through(CharacterFilter.class);
		filter("/*").through(CorePersistFilter.class);
		bind(StrutsPrepareAndExecuteFilter.class).in(Singleton.class);
		filter("/*").through(StrutsPrepareAndExecuteFilter.class);	
		filter("/*").through(AuthFilter.class);
		bind(WebSocket.class).in(Singleton.class);
		serve("/websocket.do").with(WebSocket.class);
	}
}
