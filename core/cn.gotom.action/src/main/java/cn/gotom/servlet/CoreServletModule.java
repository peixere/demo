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
		Map<String, String> characterParams = new HashMap<String, String>();
		characterParams.put("encoding", "utf-8");
		characterParams.put("forceEncoding", "true");
		filter("/*").through(CharacterFilter.class, characterParams);
		filter("/*").through(CorePersistFilter.class);
		bind(StrutsPrepareAndExecuteFilter.class).in(Singleton.class);
		filter("/*").through(StrutsPrepareAndExecuteFilter.class);	
		filter("/*").through(AuthFilter.class);
		bind(WebSocket.class).in(Singleton.class);
		serve("/websocket.do").with(WebSocket.class);
	}
}
