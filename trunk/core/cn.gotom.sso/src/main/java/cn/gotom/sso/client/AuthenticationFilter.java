package cn.gotom.sso.client;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.gotom.sso.SSOException;
import cn.gotom.sso.Ticket;
import cn.gotom.sso.TicketImpl;
import cn.gotom.sso.TicketValidator;
import cn.gotom.sso.filter.AuthenticationIgnoreFilter;
import cn.gotom.sso.util.CommonUtils;
import cn.gotom.sso.util.UrlUtils;

public class AuthenticationFilter extends AuthenticationIgnoreFilter implements TicketValidator
{

	/**
	 * 可选，接入服务器名，为空测从哪来回哪去
	 */
	private String serverName;
	/**
	 * 可选，接入服务验证返回URL，为空测从哪来回哪去
	 */
	private String service;

	private static String serverLogoutUrl;

	public static String getServerLogoutUrl()
	{
		return serverLogoutUrl;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		super.init(filterConfig);
		setServerName(getInitParameter(filterConfig, "serverName", null));
		setService(getInitParameter(filterConfig, "service", null));
		CommonUtils.assertNotNull(this.getServerLoginUrl(), serverLoginUrlParameter + " cannot be null.");
		setServerLoginUrl(getInitParameter(filterConfig, serverLoginUrlParameter, null));
		if (this.getServerLoginUrl().startsWith(contextPath))
		{
			setServerLoginUrl(filterConfig.getServletContext().getContextPath() + serverLoginUrl.substring(contextPath.length(), serverLoginUrl.length()));
			log.info("Property [serverLoginUrl] value [" + serverLoginUrl + "]");
		}
		serverLogoutUrl = this.getServerLoginUrl() + (getServerLoginUrl().indexOf("?") == -1 ? "?" : "&") + TicketValidator.Method + "=" + TicketValidator.Logout;
		log.debug("init");
	}

	protected void doValidate(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException
	{
		filterChain.doFilter(request, response);
	}

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException
	{
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;
		String url = UrlUtils.buildUrl(request);
		log.debug(url);
		if (isIgnore(url))
		{
			filterChain.doFilter(request, response);
			return;
		}
		Ticket ticket = getTicket(request);
		String ticketId = CommonUtils.safeGetParameter(request, this.getTicketParameterName());
		if (ticket != null && CommonUtils.isBlank(ticketId))
		{
			ticketId = ticket.getId();
		}
		if (!CommonUtils.isEmpty(ticketId))
		{
			try
			{
				final String serverUrl = constructServerUrl(request, response);
				ticket = validate(ticketId, serverUrl);
				if (ticket != null)
				{
					this.addTicket(request, ticket);
				}
			}
			catch (SSOException e)
			{
				log.error("validate ticket [" + ticketId + "] error", e);
			}
		}
		if (ticket != null)
		{
			if (CommonUtils.isNotBlank(request.getQueryString()))
			{
				int location = request.getQueryString().indexOf(getTicketParameterName() + "=");
				if (location != -1)
				{
					String serviceUrl = constructServiceUrl(request, response);
					response.sendRedirect(serviceUrl);
					return;
				}
			}
			doValidate(new TicketRequestWrapper(request, ticket), response, filterChain);
			return;
		}
		final String serviceUrl = constructServiceUrl(request, response);
		log.debug("serviceUrl to \"" + serviceUrl + "\"");
		final String serverUrl = constructServerUrl(request, response);
		final String urlToRedirectTo = CommonUtils.constructRedirectUrl(serverUrl, this.getServiceParameterName(), serviceUrl);
		log.debug("redirecting to \"" + urlToRedirectTo + "\"");
		response.sendRedirect(urlToRedirectTo);
	}

	@Override
	public void destroy()
	{

	}

	@Override
	public Ticket validate(String ticketId, String serverLoginUrl) throws SSOException
	{
		String queryString = TicketValidator.Method + "=" + TicketValidator.Validate + "&" + this.getTicketParameterName() + "=" + ticketId;
		String url = serverLoginUrl + (serverLoginUrl.indexOf("?") >= 0 ? "&" : "?") + queryString;
		String jsonString = CommonUtils.getResponseFromServer(url, "utf-8", ticketId);
		Ticket ticket = TicketImpl.parseFromJSON(jsonString);
		if (ticket == null)
		{
			log.debug(" validateUrl: " + url);
		}
		return ticket;
	}

	protected Ticket getTicket(final HttpServletRequest request)
	{
		final HttpSession session = request.getSession();
		String id = getTicketParameterName();
		final TicketImpl ticket = (TicketImpl) (session == null ? request.getAttribute(id) : session.getAttribute(id));
		return ticket;
	}

	protected void addTicket(final HttpServletRequest request, Ticket ticket)
	{
		if (ticket != null)
		{
			final HttpSession session = request.getSession();
			String id = getTicketParameterName();
			session.setAttribute(id, ticket);
		}
	}

	protected final String constructServiceUrl(final HttpServletRequest request, final HttpServletResponse response)
	{
		return CommonUtils.constructServiceUrl(request, response, this.getService(), this.getServerName(), this.getTicketParameterName(), this.getEncodeServiceUrl());
	}

	protected final String constructServerUrl(HttpServletRequest request, HttpServletResponse response)
	{
		String serverLoginUrl = this.getServerLoginUrl();
		final StringBuilder buffer = new StringBuilder();
		if (CommonUtils.isNotBlank(serverLoginUrl))
		{
			if (!serverLoginUrl.startsWith("https://") && !serverLoginUrl.startsWith("http://"))
			{
				buffer.append(constructServerName(request));
			}
		}
		else
		{
			buffer.append(constructServerName(request));
		}
		buffer.append(serverLoginUrl);
		return buffer.toString();
	}

	protected final String constructServerName(HttpServletRequest request)
	{
		final StringBuilder buffer = new StringBuilder();
		String serverName = this.getServerName();
		if (CommonUtils.isNotBlank(serverName))
		{
			if (!serverName.startsWith("https://") && !serverName.startsWith("http://"))
			{
				buffer.append(request.isSecure() ? "https://" : "http://");
			}
			buffer.append(serverName);
		}
		else
		{
			buffer.append(CommonUtils.getServerName(request));
		}
		return buffer.toString();
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

}
