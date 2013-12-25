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
import cn.gotom.sso.client.AuthenticationFilter;
import cn.gotom.sso.util.UrlUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ValidationFilter extends AuthenticationFilter
{

	@Inject
	protected AuthenticationService authService;

	@Inject
	protected DataInitializeService dataInitializeService;

	private FilterConfig filterConfig;

	private String[] pluginsPaths;

	private String plugins;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		this.filterConfig = filterConfig;
		super.init(filterConfig);
		initPlugins();
		dataInitializeService.init();
		log.debug("init");
	}

//	protected boolean isIgnore(String url)
//	{
//		if (!super.isIgnore(url))
//		{
//			return authService.isIgnore(url);
//		}
//		else
//		{
//			return true;
//		}
//	}

	protected void doValidate(final ServletRequest req, final ServletResponse res, final FilterChain filterChain) throws IOException, ServletException
	{
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;
		setPlugins(request);
		String url = UrlUtils.buildUrl(request);
		if (authService.validation(request.getRemoteUser(), url))
		{
			filterChain.doFilter(req, res);
		}
		else
		{
			log.warn(request.getRemoteUser() + " 403: " + url);
			response.setStatus(403);
			request.setAttribute("url", url);
			request.getRequestDispatcher("/WEB-INF/view/error/403.jsp").forward(request, response);
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
			log.info("plugins: " + plugins);
		}
		request.setAttribute("plugins", plugins);
	}

}
