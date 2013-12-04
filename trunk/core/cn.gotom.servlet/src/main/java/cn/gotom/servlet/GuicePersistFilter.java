package cn.gotom.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.gotom.dao.PersistenceLifeCycle;
import cn.gotom.web.util.filter.AbstractConfigurationFilter;

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
			log.info("startService");
		}
		catch (Exception ex)
		{
			log.error("程序启动异常", ex);
		}
	}

	public void destroy()
	{
		log.info("stopService");
		this.manager.stopService();
		log.info(this.getClass().getName());
	}

	public void doFilter(final ServletRequest sRequest, final ServletResponse sResponse, final FilterChain filterChain) throws IOException, ServletException
	{
		final HttpServletRequest request = (HttpServletRequest) sRequest;
		final HttpServletResponse response = (HttpServletResponse) sResponse;
		try
		{
			this.manager.beginUnitOfWork();
			filterChain.doFilter(request, response);
		}
		catch (Exception ex)
		{
			log.error("程序异常", ex);
			response.setStatus(500);
			// request.setAttribute("java.lang.Throwable", ex);
			request.getRequestDispatcher("/final/500.jsp").forward(request, response);
		}
		finally
		{
			this.manager.endUnitOfWork();
		}
	}
}
