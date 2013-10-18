package cn.gotom.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import cn.gotom.dao.PersistenceLifeCycle;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GuicePersistFilter extends AbstractConfigurationFilter
{
	protected PersistenceLifeCycle manager;

	@Inject
	public GuicePersistFilter(PersistenceLifeCycle manager)
	{
		this.manager = manager;
	}

	public void init(FilterConfig filterConfig) throws ServletException
	{
		try
		{
			this.manager.startService();
			log.info("=========== startService ===========");
		}
		catch (Exception ex)
		{
			log.error("程序启动异常", ex);
		}
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
