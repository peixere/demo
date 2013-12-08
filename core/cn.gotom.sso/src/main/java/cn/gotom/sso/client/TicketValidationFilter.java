package cn.gotom.sso.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import cn.gotom.sso.SSOException;
import cn.gotom.sso.Ticket;
import cn.gotom.sso.TicketImpl;
import cn.gotom.sso.TicketValidator;
import cn.gotom.sso.util.CommonUtils;
import cn.gotom.websocket.Client;

public class TicketValidationFilter extends AuthenticationFilter implements TicketValidator
{

	private Client client;
	private String webSocketUrl;

	@Override
	protected void initInternal(FilterConfig filterConfig) throws ServletException
	{
		super.initInternal(filterConfig);
		setWebSocketUrl(getInitParameter(filterConfig, "webSocketUrl", null));
		CommonUtils.assertNotNull(this.getWebSocketUrl(), "webSocketUrl cannot be null.");
	}

	@Override
	public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException
	{
		connectionWebSocket(servletRequest);
		super.doFilter(servletRequest, servletResponse, filterChain);
	}

	private void connectionWebSocket(final ServletRequest servletRequest)
	{
		if (client == null)
		{
			if (!webSocketUrl.startsWith("ws://"))
			{
				String serverName = servletRequest.getServerName();
				int serverPort = servletRequest.getServerPort();
				webSocketUrl = "ws://" + serverName + ":" + serverPort + (webSocketUrl.startsWith("/") ? "" : "/") + webSocketUrl;
				log.info(webSocketUrl);
			}
			try
			{
				client = new Client(new URI(webSocketUrl));
				client.connect();
			}
			catch (URISyntaxException e)
			{
				log.debug("连接WebSocket服务器异常", e);
			}
		}
	}

	@Override
	public Ticket validate(String ticketId, String service) throws SSOException
	{
		Ticket ticket = new TicketImpl(ticketId);
		client.send(ticket.toJSON());
		return null;
	}

	public String getWebSocketUrl()
	{
		return webSocketUrl;
	}

	public void setWebSocketUrl(String webSocketUrl)
	{
		this.webSocketUrl = webSocketUrl;
	}

}
