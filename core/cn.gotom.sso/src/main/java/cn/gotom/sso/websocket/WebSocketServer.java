package cn.gotom.sso.websocket;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;
import org.apache.log4j.Logger;

public class WebSocketServer extends WebSocketServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final Logger log = Logger.getLogger(getClass());

	/**
	 * 客户端集合
	 */
	private static final List<MessageInbound> socketList = new ArrayList<MessageInbound>();

	private final Listener<Message, CharBuffer> receiveListener = new Listener<Message, CharBuffer>()
	{

		@Override
		public void onListener(Message sender, CharBuffer msg)
		{
			receive(sender, msg);
		}
	};

	protected void receive(Message sender, CharBuffer buffer)
	{
		log.debug(buffer.toString());
		WsOutbound outbound = sender.getWsOutbound();
		try
		{
			outbound.writeTextMessage(buffer.append(" ok "));
			outbound.flush();
		}
		catch (IOException e)
		{
			log.error("", e);
		}

	}

	public static List<MessageInbound> getMessageList()
	{
		List<MessageInbound> messageList = new ArrayList<MessageInbound>(socketList);
		return messageList;
	}

	public void add(MessageInbound messageInbound)
	{
		socketList.add(messageInbound);
	}

	public void remove(MessageInbound messageInbound)
	{
		socketList.remove(messageInbound);
	}

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		log.debug("init");
	}

	@Override
	public void destroy()
	{
		log.debug("destroy");
		super.destroy();
	}

	@Override
	protected StreamInbound createWebSocketInbound(String arg0, HttpServletRequest request)
	{
		request.getHeaderNames();
		log.debug(arg0);
		Message message = new Message(this, request.getHeaderNames());
		message.setReceiveListener(receiveListener);
		return message;
	}

}
