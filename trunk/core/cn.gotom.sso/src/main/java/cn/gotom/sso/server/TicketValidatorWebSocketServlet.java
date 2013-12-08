package cn.gotom.sso.server;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;

import cn.gotom.websocket.Message;
import cn.gotom.websocket.SocketServlet;

public class TicketValidatorWebSocketServlet extends SocketServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected StreamInbound createWebSocketInbound(String arg0, HttpServletRequest request)
	{
		request.getHeaderNames();
		log.debug(arg0);
		Message message = new Message(request.getHeaderNames());
		return message;
	}
}
