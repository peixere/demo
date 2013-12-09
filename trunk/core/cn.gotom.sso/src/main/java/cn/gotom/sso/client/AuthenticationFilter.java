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
import cn.gotom.sso.filter.AbstractAuthenticationFilter;
import cn.gotom.sso.util.CommonUtils;
import cn.gotom.sso.util.UrlUtils;

public class AuthenticationFilter extends AbstractAuthenticationFilter implements TicketValidator
{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		super.init(filterConfig);
		CommonUtils.assertNotNull(this.getServerUrl(), serverUrlParameter + " cannot be null.");
	}

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException
	{
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;
		String url = UrlUtils.buildUrl(request);
		if (isIgnore(url))
		{
			filterChain.doFilter(request, response);
			return;
		}
		Ticket ticket = getTicket(request);
		if (ticket == null)
		{
			final String ticketId = CommonUtils.safeGetParameter(request, this.getTicketParameterName());
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
		}
		if (ticket != null)
		{
			filterChain.doFilter(new TicketRequestWrapper(request, ticket), response);
			return;
		}
		final String serviceUrl = constructServiceUrl(request, response);
		log.debug("Constructed service url: " + serviceUrl);
		final String serverUrl = constructServerUrl(request, response);
		log.debug("Constructed server url: " + serverUrl);
		final String urlToRedirectTo = CommonUtils.constructRedirectUrl(serverUrl, serviceParameter, serviceUrl);
		if (log.isDebugEnabled())
		{
			log.debug("redirecting to \"" + urlToRedirectTo + "\"");
		}
		response.sendRedirect(urlToRedirectTo);
	}

	private String constructServerUrl(HttpServletRequest request, HttpServletResponse response)
	{
		String serverUrl = this.getServerUrl();
		final StringBuilder buffer = new StringBuilder();
		if (CommonUtils.isNotBlank(serverUrl))
		{
			if (!serverUrl.startsWith("https://") && !serverUrl.startsWith("http://"))
			{
				buffer.append(CommonUtils.getServerName(request));
			}
		}
		else
		{
			buffer.append(CommonUtils.getServerName(request));
		}
		buffer.append(serverUrl);
		return buffer.toString();
	}

	@Override
	public void destroy()
	{

	}

	@Override
	public Ticket validate(String ticketId, String serverUrl) throws SSOException
	{
		String queryString = TicketValidator.Method + "=" + TicketValidator.Validate + "&" + this.getTicketParameterName() + "=" + ticketId;
		String url = serverUrl + (serverUrl.indexOf("?") >= 0 ? "&" : "?") + queryString;
		log.debug("validateUrl generated: " + url);
		String jsonString = CommonUtils.getResponseFromServer(url, "utf-8");
		Ticket ticket = TicketImpl.parseFromJSON(jsonString);
		return ticket;
	}

	protected Ticket getTicket(final HttpServletRequest request)
	{
		final HttpSession session = request.getSession(false);
		String id = getTicketParameterName();
		final TicketImpl ticket = (TicketImpl) (session == null ? request.getAttribute(id) : session.getAttribute(id));
		return ticket;
	}

	protected void addTicket(final HttpServletRequest request, Ticket ticket)
	{
		if (ticket != null)
		{
			final HttpSession session = request.getSession(false);
			String id = getTicketParameterName();
			session.setAttribute(id, ticket);
		}
	}

	protected final String constructServiceUrl(final HttpServletRequest request, final HttpServletResponse response)
	{
		return CommonUtils.constructServiceUrl(request, response, this.getService(), this.getServerName(), this.getTicketParameterName(), this.getEncodeServiceUrl());
	}
}
