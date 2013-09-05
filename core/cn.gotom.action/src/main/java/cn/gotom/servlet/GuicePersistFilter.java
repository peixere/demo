package cn.gotom.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import cn.gotom.dao.PersistenceLifeCycle;

public class GuicePersistFilter implements Filter
{
	protected final Logger log = Logger.getLogger(getClass());
	protected PersistenceLifeCycle manager;

	public void init(FilterConfig filterConfig) throws ServletException
	{
		Enumeration<String> names = filterConfig.getInitParameterNames();
		while (names.hasMoreElements())
		{
			log.info(filterConfig.getInitParameter(names.nextElement()));
		}
		this.manager.startService();
		log.info("=========== startService ===========");
	}

	public void destroy()
	{
		log.info("=========== stopService ===========");
		this.manager.stopService();
		log.info(this.getClass().getName());
	}

	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException
	{
		try
		{
			this.manager.beginUnitOfWork();
			filterChain.doFilter(servletRequest, servletResponse);
		}
		finally
		{
			this.manager.endUnitOfWork();
		}
	}
}
