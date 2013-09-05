package cn.gotom.servlet;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

import cn.gotom.servlet.websocket.WebSocket;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

public class CoreServletModule extends ServletModule
{
	@Override
	protected void configureServlets()
	{
		filter("*").through(CorePersistFilter.class);
		bind(StrutsPrepareAndExecuteFilter.class).in(Singleton.class);
		filter("/*").through(StrutsPrepareAndExecuteFilter.class);
		Map<String, String> params = new HashMap<String, String>();
		params.put("coffee", "Espresso");
		params.put("site", "google.com");		
		filter("/*").through(AuthFilter.class, params);
		bind(WebSocket.class).in(Singleton.class);
		serve("/websocket.do").with(WebSocket.class);
	}
}
