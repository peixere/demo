package cn.gotom.sso;

import java.util.HashMap;

public class TicketMap extends HashMap<String, Ticket>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Ticket remove(String key)
	{
		return super.remove(key);
	}
}
