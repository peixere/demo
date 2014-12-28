package cn.gotom.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;

import cn.gotom.dao.PersistenceLifeCycle;
import cn.gotom.service.InitializeService;
import cn.gotom.sso.filter.AbstractConfigurationFilter;
import cn.gotom.sso.util.CommonUtils;
import cn.gotom.sso.util.UrlUtils;
import cn.gotom.util.PasswordEncoder;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
class GuicePersistFilter extends AbstractConfigurationFilter
{
	@Inject
	protected PasswordEncoder passwordEncoder;
	protected PersistenceLifeCycle manager;

	private String[] initServices = new String[0];

	@Inject
	public GuicePersistFilter(PersistenceLifeCycle manager)
	{
		this.manager = manager;
	}

	public void init(FilterConfig filterConfig) throws ServletException
	{
		try
		{
			super.init(filterConfig);
			this.manager.startService();
			passwordEncoder.setEncodingAlgorithm(encodingAlgorithm);
			log.info("startService");
			initializeService();
		}
		catch (Exception ex)
		{
			log.error("程序启动异常", ex);
		}
	}

	private void initializeService()
	{
		String initializeService = getInitParameter(filterConfig, "initializeService", null);
		if (CommonUtils.isNotEmpty(initializeService))
		{
			String tmp = initializeService.trim().replaceAll("；", ";");
			tmp = tmp.replaceAll(",", ";");
			tmp = tmp.replaceAll("，", ";");
			tmp = tmp.replaceAll("\n", ";");
			tmp = tmp.replaceAll(";;", ";");
			tmp = tmp.replaceAll(" ", "").replaceAll("	", "");
			initServices = tmp.split(";");
			for (String name : initServices)
			{
				try
				{
					Class<?> clazz = Class.forName(name.trim());
					InitializeService initService = (InitializeService) AuthenticationListener.injector.getInstance(clazz);
					initService.init();
					log.debug(name + ".init()");
				}
				catch (Throwable e)
				{
					log.warn(name + " " + e.getMessage());
				}
			}
		}
	}

	private void destroyService()
	{
		for (String name : initServices)
		{
			try
			{
				Class<?> clazz = Class.forName(name.trim());
				InitializeService initService = (InitializeService) AuthenticationListener.injector.getInstance(clazz);
				initService.destroy();
				log.debug(name + ".destroy()");
			}
			catch (Throwable e)
			{
				log.warn(name + " " + e.getMessage());
			}
		}
	}

	public void destroy()
	{
		destroyService();
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
			Exception e = (Exception) request.getAttribute("javax.servlet.error.exception");
			if (e != null)
			{
				forwardError(request, response, e);
			}
		}
		catch (ClientAbortException ex)
		{
			log.debug("", ex);
		}
		catch (Error ex)
		{
			forwardError(request, response, ex);
		}
		catch (Exception ex)
		{
			forwardError(request, response, ex);
		}
		finally
		{
			this.manager.endUnitOfWork();
		}
	}

	private void forwardError(final HttpServletRequest request, final HttpServletResponse response, Throwable ex) throws ServletException, IOException
	{
		String url = UrlUtils.buildUrl(request);
		log.warn(request.getRemoteUser() + " Exception：" + url);
		request.setAttribute("url", url);
		request.setAttribute("errorMsg", ex.getMessage());
		log.error("程序异常", ex);
		response.setStatus(500);
		try
		{
			request.setAttribute("java.lang.Throwable", ex);
			request.getRequestDispatcher("/WEB-INF/view/error/500.jsp").forward(request, response);
			return;
		}
		catch (Exception e)
		{
		}
	}
}
