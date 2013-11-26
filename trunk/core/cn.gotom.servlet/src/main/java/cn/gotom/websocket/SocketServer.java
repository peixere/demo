package cn.gotom.websocket;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

public class SocketServer extends TimerTask
{
	private final List<MessageInbound> socketList = new ArrayList<MessageInbound>();

	private final Timer timer = new Timer(getClass().getName());

	@Override
	public void run()
	{
		try
		{
			for (MessageInbound messageInbound : socketList)
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

	public void schedule(long delay, long period)
	{
		timer.schedule(this, delay, period);
	}

	public boolean cancel()
	{
		timer.cancel();
		return super.cancel();
	}

	public List<MessageInbound> getSocketList()
	{
		return socketList;
	}

	public void add(MessageInbound messageInbound)
	{
		socketList.add(messageInbound);
	}

	public void remove(MessageInbound messageInbound)
	{
		socketList.remove(messageInbound);
	}
}
