package cn.gotom.comm.channel;

import cn.gotom.commons.Listener;

public abstract class ChannelTest
{
	/**
	 * 通道测试，收到ff后退出程序
	 * @param channel
	 * @throws Exception
	 */
	public static void start(final Channel channel) throws Exception
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
		Listener<State> statels = new Listener<State>()
		{
			@Override
			public void onListener(Object sender, State state)
			{
				System.out.println(state);
			}
		};
		channel.addStateListener(statels);
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
