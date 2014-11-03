package cn.gotom.comm.test;

import java.io.IOException;

import cn.gotom.comm.server.Server;
import cn.gotom.comm.server.TcpServer;
import cn.gotom.commons.Listener;

public class TcpServerTest
{

	public static void main(String[] args)
	{
		final Server server = new TcpServer("0.0.0.0", 8000);
		try
		{
			server.start();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		server.setReceiveListener(new Listener<byte[]>()
		{

			@Override
			public void onListener(Object sender, byte[] bytes)
			{
				// Channel channel = (Channel) sender;
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
		int terminalCount = server.getClientList().size();
		while (server.started())
		{
			int count = server.getClientList().size();
			if (terminalCount != count)
			{
				System.out.println("terminal count: " + count);
				terminalCount = count;
			}
		}
		server.stop();
	}

}
