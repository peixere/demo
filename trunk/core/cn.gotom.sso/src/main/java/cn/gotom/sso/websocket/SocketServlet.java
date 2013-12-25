package cn.gotom.sso.websocket;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;
import org.apache.log4j.Logger;

public class SocketServlet extends WebSocketServlet implements Runnable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final Logger log = Logger.getLogger(getClass());

	private static final List<MessageInbound> socketList = new ArrayList<MessageInbound>();

	private boolean terminateRunnable = true;

	private void terminateRunnable()
	{
		terminateRunnable = false;
	}

	@Override
	public void run()
	{
		while (terminateRunnable)
		{
			try
			{
				List<MessageInbound> messageList = getMessageInboundList();
				for (MessageInbound messageInbound : messageList)
				{
					CharBuffer buffer = CharBuffer.wrap("当前时间：" + new Date());
					WsOutbound outbound = messageInbound.getWsOutbound();
					outbound.writeTextMessage(buffer);
					outbound.flush();
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public static List<MessageInbound> getMessageInboundList()
	{
		List<MessageInbound> messageList = new ArrayList<MessageInbound>(socketList);
		return messageList;
	}

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		new Thread(this).start();
		log.debug("init");
	}

	@Override
	public void destroy()
	{
		log.debug("destroy");
		terminateRunnable();
		super.destroy();
	}

	@Override
	protected StreamInbound createWebSocketInbound(String arg0, HttpServletRequest request)
	{
		request.getHeaderNames();
		log.debug(arg0);
		Message message = new Message(request.getHeaderNames());
		return message;
	}

}
