package cn.gotom.comm.channel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import cn.gotom.annotation.Description;
import cn.gotom.commons.Listener;

@Description("TCP/IP客户端")
@ChannelType(ChannelTypeEnum.TCP)
public class TcpChannel extends ChannelImpl
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Socket socket;

	public TcpChannel()
	{
		super();
	}

	public TcpChannel(String host, int port)
	{
		super();
		this.parameters = new Parameters(host, port);
		log.debug("new TcpChannel()");
	}

	@Override
	public synchronized void connect() throws IOException
	{
		try
		{
			if (!connected())
			{
				socket = new Socket();
				socket.setKeepAlive(true);
				this.onState(State.Connecting);
				socket.connect(new InetSocketAddress(parameters.getAddress(), parameters.getPort()), 3000);
				socket.setKeepAlive(true);
				log.info("连接成功[" + this.getId() + "]SoTimeout=" + socket.getSoTimeout());
				this.onState(State.Connected);
				socket.setSoTimeout(parameters.getSoTimeout());
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
				super.connect();
			}
		}
		catch (IOException ex)
		{
			this.close();
			this.onState(State.Fail);
			log.warn(Thread.currentThread().getName() + "通道[" + getId() + "]连接异常：" + ex.getMessage(), ex);
			throw ex;
		}
	}

	@Override
	public boolean connected()
	{
		return socket != null && socket.isConnected() && !socket.isClosed();
	}

	@Override
	public void close()
	{
		try
		{
			if (socket != null)
			{
				if (socket.isConnected())
					log.info("closed[" + this.getId() + "]");
				socket.close();
			}
			socket = null;
		}
		catch (Throwable ex)
		{
			log.warn(Thread.currentThread().getName() + " close[" + this.getId() + "]", ex);
		}
		super.close();
	}

	@Override
	public String getId()
	{
		return this.parameters.toTcpString();
	}

	@Override
	public void setParameters(String... parameters)
	{
		String host = parameters[0];
		int port = Integer.parseInt(parameters[1]);
		this.parameters = new Parameters(host, port);
	}

	public static void main(String[] args) throws Exception
	{
		final Channel channel = new TcpChannel("127.0.0.1", 8090);
		channel.connect();
		Listener<byte[]> l = new Listener<byte[]>()
		{
			@Override
			public void onListener(Object sender, byte[] buffer)
			{
				String info = new String(buffer, 0, buffer.length);
				System.out.println(sender.getClass().getName() + " << " + info);
				try
				{
					channel.write(("" + System.currentTimeMillis()).getBytes());
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		};
		channel.addReceiveListener(l);
		while (true)
		{
			channel.write(new byte[] { 0, 1, 2, 3 });
		}
	}

}
