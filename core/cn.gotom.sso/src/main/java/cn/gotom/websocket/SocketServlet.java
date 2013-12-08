package cn.gotom.websocket;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.log4j.Logger;

public class SocketServlet extends WebSocketServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final Logger log = Logger.getLogger(getClass());

	private static final List<MessageInbound> socketList = new ArrayList<MessageInbound>();

	public static List<MessageInbound> getMessageInboundList()
	{
		return socketList;
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
		log.debug(arg0);
		return new Message();
	}

}
