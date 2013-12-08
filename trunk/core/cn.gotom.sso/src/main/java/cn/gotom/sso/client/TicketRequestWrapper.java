package cn.gotom.sso.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import cn.gotom.sso.TicketImpl;

public class TicketRequestWrapper extends HttpServletRequestWrapper
{

	private final TicketImpl ticket;

	public TicketRequestWrapper(HttpServletRequest request, TicketImpl ticket)
	{
		super(request);
		this.ticket = ticket;
	}

	public TicketImpl getTicket()
	{
		return ticket;
	}

	@Override
	public String getRemoteUser()
	{
		return ticket != null ? this.ticket.getUser() : null;
	}
}
