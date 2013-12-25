package cn.gotom.sso.server;

import java.nio.CharBuffer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

import cn.gotom.sso.Ticket;
import cn.gotom.sso.TicketImpl;
import cn.gotom.sso.TicketMap;
import cn.gotom.sso.websocket.Listener;
import cn.gotom.sso.websocket.Message;
import cn.gotom.sso.websocket.WebSocketServer;

public class TicketValidatorWebSocketServlet extends WebSocketServer
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Listener<TicketMap, Ticket> removeListener = new Listener<TicketMap, Ticket>()
	{

		@Override
		public void onListener(TicketMap sender, Ticket ticket)
		{
			broadcast(ticket);
		}

	};

	@Override
	public void receive(Message sender, CharBuffer msg)
	{
		Ticket response = null;
		Ticket request = null;
		try
		{
			request = TicketImpl.parseFromJSON(msg.toString());
		}
		catch (Exception ex)
		{
			log.error("", ex);
		}
		try
		{
			if (request != null)
			{
				if (TicketMap.instance.containsKey(request.getId()))
				{
					response = TicketMap.instance.get(request.getId());
				}
			}
			if (response == null)
			{
				response = new TicketImpl("");
			}
			CharBuffer buffer = CharBuffer.wrap(response.toJSON());
			WsOutbound outbound = sender.getWsOutbound();
			outbound.writeTextMessage(buffer);
			outbound.flush();
		}
		catch (Exception ex)
		{
			log.error("", ex);
		}
	}

	public void broadcast(Ticket ticket)
	{
		try
		{
			for (int i = getMessageList().size() - 1; i >= 0; i--)
			{
				try
				{
					MessageInbound message = getMessageList().get(i);
					CharBuffer buffer = CharBuffer.wrap(ticket.toJSON());
					WsOutbound outbound = message.getWsOutbound();
					outbound.writeTextMessage(buffer);
					outbound.flush();
				}
				catch (Exception ex)
				{
					log.error("", ex);
				}
			}
		}
		catch (Exception ex)
		{
			log.error("", ex);
		}
	}

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		TicketMap.instance.setRemoveListener(removeListener);
		super.init(config);
		log.debug("init");
	}
}
