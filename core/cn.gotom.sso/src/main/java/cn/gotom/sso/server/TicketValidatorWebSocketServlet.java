package cn.gotom.sso.server;

import java.nio.CharBuffer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WsOutbound;

import cn.gotom.sso.Ticket;
import cn.gotom.sso.TicketImpl;
import cn.gotom.sso.TicketMap;
import cn.gotom.websocket.Listener;
import cn.gotom.websocket.Message;
import cn.gotom.websocket.SocketServlet;

public class TicketValidatorWebSocketServlet extends SocketServlet
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
	private final Listener<Message, CharBuffer> receiveListener = new Listener<Message, CharBuffer>()
	{

		@Override
		public void onListener(Message sender, CharBuffer msg)
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
					if (ServerFilter.getTicketMap().containsKey(request.getId()))
					{
						response = ServerFilter.getTicketMap().get(request.getId());
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

	};

	public void broadcast(Ticket ticket)
	{
		try
		{
			for (int i = getMessageInboundList().size() - 1; i >= 0; i--)
			{
				try
				{
					MessageInbound message = getMessageInboundList().get(i);
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
		ServerFilter.getTicketMap().setRemoveListener(removeListener);
		super.init(config);
		log.debug("init");
	}

	@Override
	protected StreamInbound createWebSocketInbound(String arg0, HttpServletRequest request)
	{
		request.getHeaderNames();
		log.debug(arg0);
		Message message = new Message(request.getHeaderNames());
		message.setReceiveListener(receiveListener);
		return message;
	}
}
