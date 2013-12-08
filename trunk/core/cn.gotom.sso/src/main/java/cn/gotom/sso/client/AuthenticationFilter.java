package cn.gotom.sso.client;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		Ticket ticket = getTicketFromSessionOrRequest(request);
		if (ticket == null)
		{
			final String ticketId = CommonUtils.safeGetParameter(request, this.getTicketParameterName());
			try
			{
				ticket = validate(ticketId, this.getServerUrl());
			}
			catch (SSOException e)
			{
				log.error("validate ticket [" + ticketId + "] error", e);
			}
		}
		if (ticket != null)
		{
			filterChain.doFilter(request, response);
			return;
		}
		final String serviceUrl = constructServiceUrl(request, response);
		if (log.isDebugEnabled())
		{
			log.debug("Constructed service url: " + serviceUrl);
		}
		final String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.getServerUrl(), serviceParameter, serviceUrl);
		if (log.isDebugEnabled())
		{
			log.debug("redirecting to \"" + urlToRedirectTo + "\"");
		}
		response.sendRedirect(urlToRedirectTo);
	}

	@Override
	public void destroy()
	{

	}

	@Override
	public Ticket validate(String ticketId, String serverUrl) throws SSOException
	{
		return new TicketImpl(ticketId);
	}
}
