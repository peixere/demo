package cn.gotom.sso.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import cn.gotom.sso.SSOException;
import cn.gotom.sso.Ticket;
import cn.gotom.sso.TicketImpl;
import cn.gotom.sso.TicketMap;
import cn.gotom.sso.TicketValidator;
import cn.gotom.sso.util.CommonUtils;
import cn.gotom.sso.websocket.Listener;
import cn.gotom.sso.websocket.WSClient;

public class SafeAuthenticationFilter extends AuthenticationFilter implements TicketValidator
{
	private final TicketMap ticketMap = new TicketMap();

	public TicketMap getTicketMap()
	{
		return ticketMap;
	}

	private WSClient client;
	private String webSocketUrl;
	private final Listener<WSClient, String> receiveListener = new Listener<WSClient, String>()
	{

		@Override
		public void onListener(WSClient sender, String msg)
		{
			Ticket ticket = TicketImpl.parseFromJSON(msg);
			if (ticket != null && getTicketMap().containsKey(ticket.getId()))
			{
				getTicketMap().remove(ticket.getId());
			}
		}

	};

	@Override
	protected void initInternal(FilterConfig filterConfig) throws ServletException
	{
		super.initInternal(filterConfig);
		setWebSocketUrl(getInitParameter(filterConfig, "webSocketUrl", null));
		CommonUtils.assertNotNull(this.getWebSocketUrl(), "webSocketUrl cannot be null.");
		log.debug("initInternal");
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
				log.debug("webSocketUrl " + webSocketUrl);
				client = new WSClient(new URI(webSocketUrl));
				client.setReceiveListener(receiveListener);
				client.connect();
			}
			catch (URISyntaxException e)
			{
				log.debug("连接WebSocket服务器异常", e);
			}
		}
	}

	@Override
	public Ticket validate(String ticketId, String serverUrl) throws SSOException
	{
		Ticket ticket = super.validate(ticketId, serverUrl);
		if (ticket != null)
		{
			ticketMap.put(ticket.getId(), ticket);
		}
		return ticket;
	}

	@Override
	protected Ticket getTicket(final HttpServletRequest request)
	{
		Ticket ticket = super.getTicket(request);
		if (ticket != null)
		{
			ticket = this.getTicketMap().get(ticket.getId());
		}
		return ticket;
	}

	@Override
	protected void addTicket(final HttpServletRequest request, Ticket ticket)
	{
		if (ticket != null)
		{
			super.addTicket(request, ticket);
			this.getTicketMap().put(ticket.getId(), ticket);
		}
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
