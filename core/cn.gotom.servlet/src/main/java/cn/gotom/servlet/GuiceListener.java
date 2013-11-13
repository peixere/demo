package cn.gotom.servlet;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;
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

	@Override
	public Injector getInjector()
	{
		if (injector == null)
		{
			injector = createInjector();
		}
		return injector;
	}

	public List<Module> createModules()
	{
		List<Module> moduleList = new ArrayList<Module>();
		JpaPersistModule jpm = new JpaPersistModule("AppEntityManager");
		moduleList.add(jpm);
		//moduleList.add(new CorePersistModule());
		moduleList.add(new CoreServletModule());
		moduleList.add(new Struts2GuicePluginModule());
		return moduleList;
	}

	private Injector createInjector()
	{
		return Guice.createInjector(createModules());
	}
}
