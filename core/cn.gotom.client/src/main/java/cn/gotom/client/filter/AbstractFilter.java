package cn.gotom.client.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.gotom.client.Ticket;
import cn.gotom.client.util.CommonUtils;
import cn.gotom.client.util.PathMatcher;
import cn.gotom.client.util.PathMatcherAnt;

abstract class AbstractFilter extends AbstractConfigurationFilter
{

	protected final String serviceParameter = "service";
	
	/**
	 * 验证退回URL，为空测从哪来回哪去
	 */
	protected final String serviceUrlParameter = "serviceUrl";
	
	/**
	 * 验证服务器URL
	 */
	protected final String serverUrlParameter = "serverUrl";
	
	/**
	 * 忽列验证的路径
	 */
	protected final String noneParameter = "authenticationNone";

	/**
	 * 票根
	 */
	protected final String ticketParameterName = "ticket";

	protected final PathMatcher urlMatcher = new PathMatcherAnt();

	private boolean encodeServiceUrl = true;

	private String serviceUrl;
	
	private String serverUrl;

	private String[] authenticationNones;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		initInternal(filterConfig);
	}

	@Override
	public void destroy()
	{
	}


	public String getServiceUrl()
	{
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl)
	{
		this.serviceUrl = serviceUrl;
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

	public boolean isEncodeServiceUrl()
	{
		return encodeServiceUrl;
	}

	public void setEncodeServiceUrl(boolean encodeServiceUrl)
	{
		this.encodeServiceUrl = encodeServiceUrl;
	}

	private void initAuthenticationNone(FilterConfig filterConfig)
	{
		String none = getInitParameter(filterConfig, noneParameter, null);
		log.trace("Loaded " + noneParameter + " parameter: " + none);
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
		initAuthenticationNone(filterConfig);
		setServiceUrl(getInitParameter(filterConfig, serviceUrlParameter, null));
		log.trace("Loaded " + serviceUrlParameter + " parameter: " + this.getServiceUrl());
		setServerUrl(getInitParameter(filterConfig, serviceUrlParameter, null));
		log.trace("Loaded " + serverUrlParameter + " parameter: " + this.getServerUrl());		
		setEncodeServiceUrl(CommonUtils.parseBoolean(getInitParameter(filterConfig, "encodeServiceUrl", "true")));
		log.trace("Loading encodeServiceUrl property: " + this.encodeServiceUrl);
	}

	protected Ticket getTicketFromSessionOrRequest(final ServletRequest servletRequest)
	{
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpSession session = request.getSession(false);
		final Ticket ticket = (Ticket) (session == null ? request.getAttribute(Ticket.CONST_TICKET) : session.getAttribute(Ticket.CONST_TICKET));
		return ticket;
	}

	protected final String constructServiceUrl(final HttpServletRequest request, final HttpServletResponse response)
	{
		return CommonUtils.constructServiceUrl(request, response, this.serviceUrl, this.ticketParameterName, this.encodeServiceUrl);
	}
	protected final String constructRedirectUrl(final HttpServletRequest request, final HttpServletResponse response)
	{
		return CommonUtils.constructRedirectUrl(this.getServerUrl(), serviceParameter, this.getServiceUrl());
	}
}
