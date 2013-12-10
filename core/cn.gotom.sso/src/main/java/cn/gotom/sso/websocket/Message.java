package cn.gotom.sso.websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Enumeration;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;
import org.apache.log4j.Logger;

public class Message extends MessageInbound
{
	protected final Logger log = Logger.getLogger(getClass());
	private final Enumeration<String> headers;
	private Listener<Message, CharBuffer> receiveListener;

	public Message(Enumeration<String> headers)
	{
		this.headers = headers;
	}

	@Override
	protected void onBinaryMessage(ByteBuffer arg0) throws IOException
	{
		log.debug(arg0.toString());
	}

	/**
	 * 服务器接收到的客户端消息
	 */
	@Override
	protected void onTextMessage(CharBuffer msg) throws IOException
	{
		if (receiveListener != null)
		{
			receiveListener.onListener(this, msg);
		}
		else
		{
			log.debug(msg.toString());
		}
	}

	@Override
	protected void onClose(int status)
	{
		log.debug(this);
		SocketServlet.getMessageInboundList().remove(this);
		super.onClose(status);
	}

	@Override
	protected void onOpen(WsOutbound outbound)
	{
		log.debug(outbound);
		super.onOpen(outbound);
		SocketServlet.getMessageInboundList().add(this);
	}

	public Listener<Message, CharBuffer> getReceiveListener()
	{
		return receiveListener;
	}

	public void setReceiveListener(Listener<Message, CharBuffer> receiveListener)
	{
		this.receiveListener = receiveListener;
	}

	public Enumeration<String> getHeaders()
	{
		return headers;
	}

}
