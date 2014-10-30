package cn.gotom.comm.channel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import cn.gotom.commons.Listener;

/**
 * 通道定时接收虚类
 * 
 * @author <a href="mailto:pqixere@qq.com">裴绍国</a>
 * @version 2013-10-16
 */
public abstract class ChannelImpl extends ChannelBase
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected DataInputStream in;

	protected DataOutputStream out;

	private Timer receiveTimer;

	@Override
	public void connect() throws IOException
	{
		if (receiveTimer != null)
		{
			receiveTimer.cancel();
		}
		receiveTimer = new Timer("Timer Receive");
		TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				receive();
			}
		};
		receiveTimer.schedule(task, 1000, 1);
	}

	@Override
	protected int read(byte[] bytes) throws Exception
	{
		return in.read(bytes);
	}

	@Override
	public void write(byte[] bytes) throws IOException
	{
		out.write(bytes);
		lastTimeMillis = System.currentTimeMillis();
		onMessageListener(bytes, true);
	}

	@Override
	public void close()
	{
		try
		{
			if (receiveTimer != null)
			{
				receiveTimer.cancel();
			}
			receiveTimer = null;
			if (in != null)
			{
				in.close();
			}
			if (out != null)
			{
				out.close();
			}
			in = null;
			out = null;
			this.onState(State.Close);
		}
		catch (Exception ex)
		{
			log.warn("close[" + this.getId() + "]", ex);
		}
	}

	/**
	 * 通道测试，收到ff后退出程序
	 * 
	 * @param channel
	 * @throws Exception
	 */
	public static void test(final Channel channel) throws Exception
	{
		Listener<String> l = new Listener<String>()
		{
			@Override
			public void onListener(Object sender, String buffer)
			{
				if (buffer.equalsIgnoreCase("<< ff"))
				{
					channel.close();
				}
			}
		};
		channel.setMessageListener(l);
		channel.connect();
		while (channel.connected())
		{
			try
			{
				channel.write(("" + System.currentTimeMillis()).getBytes());
				Thread.sleep(2000);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
