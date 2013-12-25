package cn.gotom.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

import cn.gotom.service.ServiceModule;
import cn.gotom.sso.filter.AbstractCommonFilter;
import cn.gotom.sso.filter.CharacterFilter;
import cn.gotom.sso.server.ServerFilter;
import cn.gotom.sso.util.CommonUtils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.google.inject.struts2.Struts2GuicePluginModule;

/**
 * 
 * 
 * @author peixere@qq.com
 */
public class GuiceListener extends GuiceServletContextListener
{
	protected final Logger log = Logger.getLogger(this.getClass());

	protected static Injector injector;

	private String serverLoginUrl;

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent)
	{
		serverLoginUrl = servletContextEvent.getServletContext().getInitParameter(AbstractCommonFilter.serverLoginUrlParameter);
		if (CommonUtils.isNotEmpty(serverLoginUrl) && serverLoginUrl.startsWith(AbstractCommonFilter.contextPath))
		{
			serverLoginUrl = serverLoginUrl.substring(AbstractCommonFilter.contextPath.length(), serverLoginUrl.length());
		}
		if (CommonUtils.isEmpty(serverLoginUrl))
		{
			serverLoginUrl = "/login.do";
		}
		super.contextInitialized(servletContextEvent);
		log.info("contextInitialized");
	}

	@Override
	public Injector getInjector()
	{
		if (injector == null)
		{
			injector = Guice.createInjector(createModules());
		}
		return injector;
	}

	protected List<Module> createModules()
	{
		ServletModule servletModule = new ServletModule()
		{
			@Override
			protected void configureServlets()
			{
//				bind(WebSocketServer.class).in(Singleton.class);
//				serve("/websocket.do").with(WebSocketServer.class);

				bind(CharacterFilter.class).in(Singleton.class);
				filter("/*").through(CharacterFilter.class);

				// filter("/*").through(GuicePersistMultiModulesFilter.class);//私有多多 Multiple Modules
				filter("/*").through(GuicePersistFilter.class);

				// bind(AuthenticationServiceFilter.class).in(Singleton.class);
				// filter("/authService").through(AuthenticationServiceFilter.class);

				bind(ServerFilter.class).in(Singleton.class);
				filter(serverLoginUrl).through(ServerFilter.class);
				bind(ValidationFilter.class).in(Singleton.class);
				filter("/*").through(ValidationFilter.class);

				bind(StrutsPrepareAndExecuteFilter.class).in(Singleton.class);
				filter("/*").through(StrutsPrepareAndExecuteFilter.class);

			}
		};
		List<Module> moduleList = new ArrayList<Module>();
		JpaPersistModule jpm = new JpaPersistModule("AppEntityManager");
		try
		{
			Properties properties = new Properties();
			properties.load(this.getClass().getResourceAsStream("/jdbc.properties"));
			jpm.properties(properties);
		}
		catch (Exception ex)
		{
			log.error("", ex);
		}
		moduleList.add(jpm);
		// moduleList.add(new CorePersistModule());
		moduleList.add(new ServiceModule());
		moduleList.add(servletModule);
		moduleList.add(new Struts2GuicePluginModule());
		return moduleList;
	}
}
