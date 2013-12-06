package cn.gotom.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.gotom.service.AuthenticationService;
import cn.gotom.service.DataInitializeService;
import cn.gotom.sso.filter.AbstractConfigurationFilter;
import cn.gotom.sso.util.UrlUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ValidationFilter extends AbstractConfigurationFilter
{

	@Inject
	protected AuthenticationService authService;

	@Inject
	protected DataInitializeService dataInitializeService;

	private FilterConfig filterConfig;

	private String[] pluginsPaths;

	private String plugins;


	public void destroy()
	{

	}

	public void doFilter(final ServletRequest sRequest, final ServletResponse sResponse, final FilterChain filterChain) throws IOException, ServletException
	{
		try
		{
			final HttpServletRequest request = (HttpServletRequest) sRequest;
			final HttpServletResponse response = (HttpServletResponse) sResponse;
			setPlugins(request);
			String url = UrlUtils.buildUrl(request);
			if (this.isIgnore(url) || authService.validation(request.getRemoteUser(), url))
			{
				filterChain.doFilter(sRequest, sResponse);
			}
			else
			{
				log.warn(request.getRemoteUser() + " 403：" + url);
				response.setStatus(403);
				request.setAttribute("url", url);
				request.getRequestDispatcher("/WEB-INF/view/error/403.jsp").forward(request, response);
			}
		}
		finally
		{

		}
	}

	private void initPlugins()
	{
		String pluginsPath = getInitParameter(filterConfig, "pluginsPath", "/plugins");
		log.info("pluginsPath=" + pluginsPath);
		String path = filterConfig.getServletContext().getRealPath(pluginsPath);
		File file = new File(path);
		if (file.exists() && file.isDirectory())
		{
			this.pluginsPaths = file.list();
		}
	}

	private void setPlugins(final HttpServletRequest request)
	{
		if (plugins == null && pluginsPaths != null)
		{
			plugins = "";
			for (String name : pluginsPaths)
			{
				plugins += "Ext.Loader.setPath('" + name + "', '" + request.getContextPath() + "/plugins/" + name + "/classes');\n\t";
			}
			log.info("plugins：" + plugins);
		}
		request.setAttribute("plugins", plugins);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		this.filterConfig = filterConfig;
		super.init(filterConfig);
		initPlugins();
		dataInitializeService.init();
	}

}
