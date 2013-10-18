package cn.gotom.servlet.websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;
import org.apache.log4j.Logger;

public class Message extends MessageInbound
{
	protected final Logger log = Logger.getLogger(getClass());

	@Override
	protected void onBinaryMessage(ByteBuffer arg0) throws IOException
	{
		log.debug(arg0.toString());
	}

	@Override
	protected void onTextMessage(CharBuffer msg) throws IOException
	{
		for (MessageInbound messageInbound : WebSocket.getSocketServer().getSocketList())
		{
			CharBuffer buffer = CharBuffer.wrap("你好：" + msg.toString());
			log.debug(buffer);
			WsOutbound outbound = messageInbound.getWsOutbound();
			outbound.writeTextMessage(buffer);
			outbound.flush();
		}
	}

	@Override
	protected void onClose(int status)
	{
		log.debug(this);
		WebSocket.getSocketServer().remove(this);
		super.onClose(status);
	}

	@Override
	protected void onOpen(WsOutbound outbound)
	{
		log.debug(outbound);
		super.onOpen(outbound);
		WebSocket.getSocketServer().add(this);
	}
}
