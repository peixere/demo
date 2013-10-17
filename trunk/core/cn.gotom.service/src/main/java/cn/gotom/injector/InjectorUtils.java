package cn.gotom.injector;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class InjectorUtils
{
	private static ResourceBundle moduleResource;
	protected static final Logger log = Logger.getLogger(InjectorUtils.class);

	protected static Injector injector;

	public static void createInjector()
	{
		moduleResource = ResourceBundle.getBundle("META-INF/guice_modules", Locale.ROOT);
		List<Module> moduleList = new ArrayList<Module>();
		for (String clazz : moduleResource.keySet())
		{
			try
			{
				Module module = (Module) Class.forName(clazz).newInstance();
				moduleList.add(module);
				log.debug("newInstance module：" + module);
			}
			catch (Exception ex)
			{
				log.error(clazz + " newInstance module error", ex);
			}
		}
		injector = Guice.createInjector(moduleList);
		// log.debug("createInjector：" + injector);
	}

	public static <T> T getInstance(Class<T> clazz)
	{
		return injector.getInstance(clazz);
	}

	public static Injector getInjector()
	{
		if (injector == null)
		{
			createInjector();
		}
		return injector;
	}
}
