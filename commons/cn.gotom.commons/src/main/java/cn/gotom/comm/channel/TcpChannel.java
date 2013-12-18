package cn.gotom.comm.channel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import cn.gotom.annotation.Description;

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
	public void write(byte[] bytes) throws IOException
	{
		try
		{
			if (!connected())
			{
				this.connect();
			}
			out.write(bytes);
			onMessageListener(bytes, true);
		}
		catch (java.net.SocketException ex)
		{
			log.error(" 通道[" + getId() + "]发送异常：" + ex.getMessage(), ex);
			this.close();
			throw new IOException(ex.getMessage(), ex);
		}
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
	public void setParameters(String... parameters)
	{
		String host = parameters[0];
		int port = Integer.parseInt(parameters[1]);
		this.parameters = new Parameters(host, port);
	}

	public static void main(String[] args) throws Exception
	{
		Channel channel = new TcpChannel("127.0.0.1", 8090);
		ChannelTest.start(channel);
	}

}
