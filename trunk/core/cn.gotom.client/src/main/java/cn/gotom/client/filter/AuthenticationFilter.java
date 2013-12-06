package cn.gotom.client.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.gotom.client.Ticket;
import cn.gotom.client.util.CommonUtils;
import cn.gotom.client.util.UrlUtils;

public class AuthenticationFilter extends AbstractFilter
{
	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		super.init(filterConfig);
		CommonUtils.assertNotNull(this.getServerUrl(), serverUrlParameter + " cannot be null.");
	}

	public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException
	{
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;
		String url = UrlUtils.buildUrl(request);
		if (isIgnore(url))
		{
			filterChain.doFilter(request, response);
			return;
		}
		final Ticket ticket = getTicketFromSessionOrRequest(request);
		if (ticket != null)
		{
			filterChain.doFilter(request, response);
			return;
		}

		final String ticketName = CommonUtils.safeGetParameter(request, ticketParameterName);
		if (CommonUtils.isNotBlank(ticketName))
		{
			filterChain.doFilter(request, response);
			return;
		}
		final String serviceUrl = constructServiceUrl(request, response);
		if (log.isDebugEnabled())
		{
			log.debug("Constructed service url: " + serviceUrl);
		}

		final String urlToRedirectTo = constructRedirectUrl(request, response);

		if (log.isDebugEnabled())
		{
			log.debug("redirecting to \"" + urlToRedirectTo + "\"");
		}
		response.sendRedirect(urlToRedirectTo);
	}
}
