package cn.gotom.servlet;

import cn.gotom.injector.InjectorUtils;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * 
 * 
 * @author peixere@qq.com
 */
public class GuiceListener extends GuiceServletContextListener
{
	@Override
	public Injector getInjector()
	{
		return InjectorUtils.getInjector();
	}
}
