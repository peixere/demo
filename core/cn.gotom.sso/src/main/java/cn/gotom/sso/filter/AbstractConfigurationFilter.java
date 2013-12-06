package cn.gotom.sso.filter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import cn.gotom.sso.TicketImpl;
import cn.gotom.sso.util.CommonUtils;
import cn.gotom.sso.util.PathMatcher;
import cn.gotom.sso.util.PathMatcherAnt;

public abstract class AbstractConfigurationFilter implements Filter
{
	protected final Logger log = Logger.getLogger(getClass());
	protected final PathMatcher urlMatcher = new PathMatcherAnt();

	protected static final String serviceParameter = "service";

	protected static final String serverUrlParameter = "serverUrl";

	private String ticketParameterName = "ticket";

	private String serviceParameterName = "service";

	private boolean encodeServiceUrl = true;

	/**
	 * 可选，接入服务器名，为空测从哪来回哪去
	 */
	private String serverName;
	/**
	 * 可选，接入服务验证返回URL，为空测从哪来回哪去
	 */
	private String service;

	/**
	 * 必选，验证服务器URL
	 */
	private String serverUrl;

	/**
	 * 忽列验证的路径
	 */
	private String[] authenticationNones;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		initInternal(filterConfig);
	}

	private void initIgnore(FilterConfig filterConfig)
	{
		String none = getInitParameter(filterConfig, "authenticationNone", null);
		log.trace("Loaded " + "authenticationNone" + " parameter: " + none);
		if (none != null)
		{
			none = none.trim().replace("；", ";");
			none = none.replace(",", ";");
			none = none.replace("，", ";");
			none = none.replace("\n", ";");
			authenticationNones = none.trim().split(";");
			for (int i = 0; i < authenticationNones.length; i++)
			{
				authenticationNones[i] = authenticationNones[i].trim();
			}
		}
	}

	protected boolean isIgnore(String url)
	{
		if (authenticationNones != null)
		{
			for (String pattern : authenticationNones)
			{
				if (urlMatcher.pathMatchesUrl(pattern.trim(), url))
				{
					return true;
				}
			}
		}
		return false;
	}

	protected void initInternal(final FilterConfig filterConfig) throws ServletException
	{
		initIgnore(filterConfig);
		setTicketParameterName(getInitParameter(filterConfig, "ticketParameterName", "ticket"));
		log.trace("Loading artifact parameter name property: " + this.getTicketParameterName());
		setServiceParameterName(getInitParameter(filterConfig, "serviceParameterName", "service"));
		log.trace("Loading serviceParameterName property: " + this.getServiceParameterName());

		setServerName(getInitParameter(filterConfig, "serverName", null));
		log.trace("Loading serverName property: " + this.serverName);
		setService(getInitParameter(filterConfig, serviceParameter, null));
		log.trace("Loaded " + serviceParameter + " parameter: " + this.getService());
		setServerUrl(getInitParameter(filterConfig, serverUrlParameter, null));
		log.trace("Loaded " + serverUrlParameter + " parameter: " + this.getServerUrl());
		setEncodeServiceUrl(CommonUtils.parseBoolean(getInitParameter(filterConfig, "encodeServiceUrl", "true")));
		log.trace("Loading encodeServiceUrl property: " + this.encodeServiceUrl);
	}

	protected TicketImpl getTicketFromSessionOrRequest(final ServletRequest servletRequest)
	{
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpSession session = request.getSession(false);
		final TicketImpl ticket = (TicketImpl) (session == null ? request.getAttribute(getTicketParameterName()) : session.getAttribute(getTicketParameterName()));
		return ticket;
	}

	protected final String constructServiceUrl(final HttpServletRequest request, final HttpServletResponse response)
	{
		return CommonUtils.constructServiceUrl(request, response, this.getService(), this.getServerName(), this.getTicketParameterName(), this.encodeServiceUrl);
	}

	protected final String getInitParameter(final FilterConfig filterConfig, final String propertyName, final String defaultValue)
	{
		String value = filterConfig.getInitParameter(propertyName);
		if (CommonUtils.isNotBlank(value))
		{
			log.info("Property [" + propertyName + "] loaded from FilterConfig.getInitParameter with value [" + value + "]");
			return value;
		}
		value = filterConfig.getServletContext().getInitParameter(propertyName);
		if (CommonUtils.isNotBlank(value))
		{
			log.info("Property [" + propertyName + "] loaded from ServletContext.getInitParameter with value [" + value + "]");
			return value;
		}
		if (CommonUtils.isEmpty(value))
		{
			value = defaultValue;
		}
		log.info("Property [" + propertyName + "] not found.  Using default value [" + defaultValue + "]");
		return defaultValue;
	}

	public boolean isEncodeServiceUrl()
	{
		return encodeServiceUrl;
	}

	public void setEncodeServiceUrl(boolean encodeServiceUrl)
	{
		this.encodeServiceUrl = encodeServiceUrl;
	}

	public String getTicketParameterName()
	{
		return ticketParameterName;
	}

	public void setTicketParameterName(String ticketParameterName)
	{
		this.ticketParameterName = ticketParameterName;
	}

	public String getServiceParameterName()
	{
		return serviceParameterName;
	}

	public void setServiceParameterName(String serviceParameterName)
	{
		this.serviceParameterName = serviceParameterName;
	}

	public String getServerName()
	{
		return serverName;
	}

	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}

	public String getService()
	{
		return service;
	}

	public void setService(String service)
	{
		this.service = service;
	}

	public String getServerUrl()
	{
		return serverUrl;
	}

	public void setServerUrl(String serverUrl)
	{
		this.serverUrl = serverUrl;
	}

	public String[] getAuthenticationNones()
	{
		return authenticationNones;
	}

	public void setAuthenticationNones(String[] authenticationNones)
	{
		this.authenticationNones = authenticationNones;
	}

}
