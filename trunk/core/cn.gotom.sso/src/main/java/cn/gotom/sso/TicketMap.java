package cn.gotom.sso;

import java.util.HashMap;

import cn.gotom.websocket.Listener;

public class TicketMap extends HashMap<String, Ticket>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Listener<TicketMap, Ticket> removeListener;

	public Ticket remove(String key)
	{
		Ticket ticket = super.remove(key);
		if (removeListener != null)
		{
			removeListener.onListener(this, ticket);
		}
		return ticket;
	}

	public Listener<TicketMap, Ticket> getRemoveListener()
	{
		return removeListener;
	}

	public void setRemoveListener(Listener<TicketMap, Ticket> removeListener)
	{
		this.removeListener = removeListener;
	}

}
