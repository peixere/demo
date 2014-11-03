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

import cn.gotom.pojos.App;
import cn.gotom.pojos.Custom;
import cn.gotom.pojos.User;
import cn.gotom.service.AuthenticationService;
import cn.gotom.service.InitializeService;
import cn.gotom.service.Service;
import cn.gotom.service.UserService;
import cn.gotom.sso.client.AuthenticationFilter;
import cn.gotom.sso.util.CommonUtils;
import cn.gotom.sso.util.UrlUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ValidationFilter extends AuthenticationFilter
{
	@Inject
	protected UserService userService;
	@Inject
	protected AuthenticationService authService;

	@Inject
	protected Service service;

	private String pluginsPath;

	private String[] pluginsPaths;

	private String plugins;

	private String debugModule = "";

	private String initializeService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		super.init(filterConfig);
		initPlugins();
		initializeService();
	}

	private void initializeService()
	{
		initializeService = getInitParameter(filterConfig, "initializeService", null);
		if (CommonUtils.isNotEmpty(initializeService))
		{
			String tmp = initializeService.trim().replace("；", ";");
			tmp = tmp.replace(",", ";");
			tmp = tmp.replace("，", ";");
			tmp = tmp.replace("\n", ";");
			tmp = tmp.replace(";;", ";");
			String[] services = tmp.split(";");
			for (String name : services)
			{
				try
				{
					Class<?> clazz = Class.forName(name);
					InitializeService initService = (InitializeService) GuiceListener.injector.getInstance(clazz);
					initService.init();
					log.debug(name + ".init() souccess");
				}
				catch (Throwable e)
				{
					log.warn(name + " " + e.getMessage());
				}
			}
			//
		}
	}

	// protected boolean isIgnore(String url)
	// {
	// if (!super.isIgnore(url))
	// {
	// return authService.isIgnore(url);
	// }
	// else
	// {
	// return true;
	// }
	// }

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException
	{
		setPlugins((HttpServletRequest) servletRequest);
		super.doFilter(servletRequest, servletResponse, filterChain);
	}

	private boolean setCurrentCustomId(final HttpServletRequest request, User user)
	{
		String customId = (String) request.getSession().getAttribute(Custom.currentCustomId);
		boolean hasAttribute = true;
		if (CommonUtils.isEmpty(customId))
		{
			hasAttribute = false;
			customId = (String) request.getParameter("customId");
		}
		if (CommonUtils.isEmpty(customId))
		{
			customId = user.getDefaultCustomId();
		}
		if (CommonUtils.isEmpty(customId))
		{
			Custom custom = authService.getDefaultCustom(user);
			if (custom != null)
			{
				customId = custom.getId();
			}
		}
		if (CommonUtils.isNotEmpty(customId))
		{
			if (!hasAttribute)
			{
				request.getSession().setAttribute(Custom.currentCustomId, customId);
			}
			if (User.ROOT.equals(user.getUsername()) || service.userHasCustom(user.getId(), customId))
			{
				return true;
			}
			else
			{
				request.getSession().removeAttribute(Custom.currentCustomId);
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	@Override
	protected void doValidate(final ServletRequest req, final ServletResponse res, final FilterChain filterChain) throws IOException, ServletException
	{
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;
		String url = UrlUtils.buildUrl(request);
		User user = userService.getByUsername(request.getRemoteUser());
		if (user != null && authService.validation(user, url, App.ROOT) && setCurrentCustomId(request, user))
		{
			request.setAttribute(User.CurrentLoginUser, user);
			filterChain.doFilter(request, response);
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
		pluginsPath = getInitParameter(filterConfig, "pluginsPath", "/plugins");
		if (!pluginsPath.endsWith("/"))
		{
			pluginsPath = pluginsPath + "/";
		}
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
		String debug = request.getParameter("debug");
		if (debug == null)
		{
			debug = "";
		}
		if (!debug.equals(debugModule))
		{
			debugModule = debug;
			plugins = null;
			initPlugins();
		}
		if ((plugins == null && pluginsPaths != null))
		{
			plugins = "...";
			StringBuffer pluginsBf = new StringBuffer();
			for (String module : pluginsPaths)
			{
				String path = filterConfig.getServletContext().getRealPath(pluginsPath + module);
				File moduleDir = new File(path);
				if (moduleDir.exists() && moduleDir.isDirectory())
				{
					File[] files = moduleDir.listFiles();
					for (File file : files)
					{
						if (file.isDirectory() && !file.getName().equals("metadata"))
						{
							pluginsBf.append("\n\tExt.Loader.setPath('" + module + "', '" + request.getContextPath() + pluginsPath + module + "/" + file.getName() + "');");
							if (module.equals(debugModule))
							{
								File[] lastDirs = file.listFiles();
								for (File lastDir : lastDirs)
								{
									if (lastDir.isDirectory())
									{
										for (String jsName : lastDir.list())
										{
											if (jsName.toLowerCase().endsWith(".js"))
											{
												jsName = jsName.substring(0, jsName.length() - 3);
												pluginsBf.append("\n\tExt.require('" + module + "." + lastDir.getName() + "." + jsName + "');");
											}
										}
									}
								}
							}
						}
					}
				}
			}
			plugins = pluginsBf.toString();
			log.info("plugins: " + plugins);
		}
		request.setAttribute("plugins", plugins);
	}
}
