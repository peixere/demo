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
import cn.gotom.service.Service;
import cn.gotom.service.UserService;
import cn.gotom.sso.client.AuthenticationFilter;
import cn.gotom.sso.util.CommonUtils;
import cn.gotom.sso.util.UrlUtils;
import cn.gotom.util.PasswordEncoder;
import cn.gotom.util.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ValidationFilter extends AuthenticationFilter
{

	@Inject
	protected PasswordEncoder passwordEncoder;
	@Inject
	protected UserService userService;
	@Inject
	protected AuthenticationService authService;

	@Inject
	protected Service service;

	private FilterConfig filterConfig;

	private String pluginsPath;

	private String[] pluginsPaths;

	private String plugins;

	private boolean debugScript;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		this.filterConfig = filterConfig;
		super.init(filterConfig);
		String encodingAlgorithm = this.getInitParameter(filterConfig, "encodingAlgorithm", "MD5");
		initPlugins();
		passwordEncoder.setEncodingAlgorithm(encodingAlgorithm);
		service.init();
		log.debug("init");
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
		if (StringUtils.isNullOrEmpty(customId))
		{
			hasAttribute = false;
			customId = (String) request.getParameter("customId");
		}
		if (StringUtils.isNullOrEmpty(customId))
		{
			customId = user.getDefaultCustomId();
		}
		if (StringUtils.isNullOrEmpty(customId))
		{
			Custom custom = authService.getDefaultCustom(user);
			if (custom != null)
			{
				customId = custom.getId();
			}
		}
		if (StringUtils.isNotEmpty(customId))
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
		boolean debug = CommonUtils.parseBoolean(request.getParameter("debug"));
		if (debugScript != debug)
		{
			debugScript = debug;
			plugins = null;
		}
		if ((plugins == null && pluginsPaths != null))
		{
			plugins = "\n\t";
			for (String name : pluginsPaths)
			{
				plugins += "Ext.Loader.setPath('" + name + "', '" + request.getContextPath() + pluginsPath + name + "/classes');\n\t";
				if (debugScript)
				{
					String path = filterConfig.getServletContext().getRealPath(pluginsPath + name + "/classes/view/");
					File file = new File(path);
					if (file.exists() && file.isDirectory())
					{
						String[] views = file.list();
						for (String view : views)
						{
							view = view.substring(0, view.length() - 3);
							plugins += "Ext.require('" + name + ".view." + view + "');\n\t";
						}
					}
				}
			}
			log.info("plugins: " + plugins);
		}
		request.setAttribute("plugins", plugins);
	}

}
