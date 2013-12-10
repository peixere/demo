package cn.gotom.websocket;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.log4j.Logger;

public class WebSocket extends WebSocketServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final Logger log = Logger.getLogger(getClass());
	private static SocketServer socketServer;

	public static SocketServer getSocketServer()
	{
		return socketServer;
	}

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		socketServer = new SocketServer();
		socketServer.schedule(10000, 1000);
		log.debug("init");
	}

	@Override
	public void destroy()
	{
		log.debug("destroy");
		socketServer.cancel();
		super.destroy();
	}

	@Override
	protected StreamInbound createWebSocketInbound(String arg0, HttpServletRequest request)
	{
		log.debug(arg0);
		return new Message();
	}

}
