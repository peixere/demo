package cn.gotom.comm.test;

import java.io.IOException;

import cn.gotom.comm.channel.Channel;
import cn.gotom.comm.server.TcpServer;
import cn.gotom.commons.Listener;

public class TcpServerTest
{

	public static void main(String[] args)
	{
		final Channel server = new TcpServer("0.0.0.0", 8000);
		try
		{
			server.connect();
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		server.addReceiveListener(new Listener<byte[]>()
		{

			@Override
			public void onListener(Object sender, byte[] bytes)
			{
				//Channel channel = (Channel) sender;
				try
				{
					server.write(bytes);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
		while (true)
		{
			;
		}
	}

}
