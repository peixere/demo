package cn.gotom.comm.channel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import cn.gotom.annotation.Description;
import cn.gotom.commons.Listener;

@Description("UDP")
@ChannelType(ChannelTypeEnum.UDP)
public class UdpChannel extends ChannelImpl
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected DatagramSocket socket;

	public UdpChannel() throws SocketException
	{
		super();
	}

	public UdpChannel(String host, int port, int localPort)
	{
		super();
		this.parameters = new Parameters(host, port, localPort);
	}

	@Override
	public synchronized void connect() throws IOException
	{
		try
		{
			if (!connected())
			{
				if (socket == null)
					socket = new DatagramSocket(parameters.getLocalPort());
				this.onState(State.Connecting);
				// socket.connect(InetAddress.getByName(host), port);
				socket.connect(new InetSocketAddress(parameters.getAddress(), parameters.getPort()));
				log.info("连接成功[" + this.getId() + "]SoTimeout=" + socket.getSoTimeout());
				this.onState(State.Connected);
				socket.setSoTimeout(parameters.getSoTimeout());
				super.connect();
			}
		}
		catch (IOException ex)
		{
			this.close();
			this.onState(State.Fail);
			log.error(Thread.currentThread().getName() + "通道[" + getId() + "]连接异常：" + ex.getMessage(), ex);
			throw ex;
		}
	}

	@Override
	protected int read(byte[] bytes) throws IOException
	{
		if (connected())
		{
			// DatagramPacket dp = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(host), port);
			DatagramPacket dp = new DatagramPacket(bytes, bytes.length);
			socket.receive(dp);
			return dp.getLength();
		}
		else
		{
			return -1;
		}
	}

	@Override
	public void write(byte[] bytes) throws IOException
	{
		try
		{
			DatagramPacket dp = new DatagramPacket(bytes, bytes.length, new InetSocketAddress(parameters.getAddress(), parameters.getPort()));
			socket.send(dp);
		}
		catch (Exception ex)
		{
			log.error(Thread.currentThread().getName() + " close[" + this.getId() + "]" + ex.getMessage());
		}
	}

	@Override
	public boolean connected()
	{
		return socket != null;// && socket.isConnected();
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
			log.error(Thread.currentThread().getName() + " close[" + this.getId() + "]", ex);
		}
		super.close();
	}

	@Override
	public String getId()
	{
		return this.parameters.toUdpString();
	}

	public static void main(String[] args) throws Exception
	{
		final Channel channel = new UdpChannel("127.0.0.1", 40001, 40002);
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
	}
}
