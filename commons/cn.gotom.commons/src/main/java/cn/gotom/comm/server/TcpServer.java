package cn.gotom.comm.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.gotom.comm.channel.Channel;
import cn.gotom.comm.channel.ChannelBase;
import cn.gotom.comm.channel.ChannelImpl;
import cn.gotom.comm.channel.ChannelTypeEnum;
import cn.gotom.comm.channel.Parameters;
import cn.gotom.comm.channel.State;

public class TcpServer extends ChannelBase
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ServerSocket socket;

	private Timer terminalTimer;
	private List<Terminal> terminalList = new ArrayList<Terminal>();
	public static final ThreadLocal<Channel> terminalPool = new ThreadLocal<Channel>();
	
	private int maxTerminalSize = 1024;
	
	public TcpServer(int port)
	{
		this("0.0.0.0", port);
	}

	public TcpServer(String host, int port)
	{
		super();
		parameters.setAddress(host);
		parameters.setPort(port);
		this.setParameters(parameters);
	}

	@Override
	public void setParameters(Parameters parameters)
	{
		super.setParameters(parameters);
		this.parameters.setChannelType(ChannelTypeEnum.TCPServer);
	}

	@Override
	public void setParameters(String... parameters)
	{
		String host = parameters[0];
		int port = Integer.parseInt(parameters[1]);
		this.parameters.setAddress(host);
		this.parameters.setPort(port);
		this.setParameters(parameters);
	}

	@Override
	public boolean connected()
	{
		return socket != null && !socket.isClosed();
	}

	@Override
	public void connect() throws IOException
	{
		try
		{
			if (!connected())
			{
				this.onState(State.Connecting);
				socket = new ServerSocket();
				socket.bind(InetSocketAddress.createUnresolved(parameters.getAddress(), parameters.getPort()));
				log.info("连接成功[" + this.getId() + "]SoTimeout=" + socket.getSoTimeout());
				this.onState(State.Connected);
				socket.setSoTimeout(parameters.getSoTimeout());
				// super.connect();
				terminalTimerStart();
			}
		}
		catch (IOException ex)
		{
			this.close();
			this.onState(State.Fail);
			log.warn(Thread.currentThread().getName() + "通道[" + getId() + "]启动异常：" + ex.getMessage(), ex);
			throw ex;
		}
	}

	// @Override
	// protected void receive()
	// {
	//
	// }

	private void terminalTimerStart()
	{
		if (terminalTimer != null)
		{
			terminalTimer.cancel();
		}
		terminalTimer = new Timer("Timer Terminal");
		TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				receiveTerminal();
			}
		};
		terminalTimer.schedule(task, 1000, 1);
	}

	private void receiveTerminal()
	{
		if (!connected())
		{
			try
			{
				Terminal terminal = new Terminal(socket.accept());
				if (terminalList.size() < maxTerminalSize)
				{
					terminalList.add(terminal);
				}
				else
				{
					terminal.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void close()
	{
		try
		{
			if (terminalTimer != null)
			{
				terminalTimer.cancel();
			}
			terminalTimer = null;
			if (socket != null)
			{
				if (!socket.isClosed())
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
	public void write(byte[] bytes) throws IOException
	{
		Channel terminal = terminalPool.get();
		if (terminal != null)
		{
			terminal.write(bytes);
		}
	}

	@Override
	protected int read(byte[] bytes) throws Exception
	{
		return 0;
	}

	class Terminal extends ChannelImpl
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected Socket socket;

		public Terminal(Socket socket)
		{
			this.socket = socket;
			this.parameters.setAddress(socket.getRemoteSocketAddress().toString());
			this.parameters.setPort(socket.getPort());
			this.connect();
		}

		@Override
		public void connect()
		{
			try
			{
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
				super.connect();
			}
			catch (IOException e)
			{
				e.printStackTrace();
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
				terminalList.remove(this);
				if (null != terminalPool.get())
				{
					terminalPool.remove();
				}
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
		protected void onReceiveListener(byte[] buffer)
		{
			if (null == terminalPool.get())
			{
				terminalPool.set(this);
			}
			TcpServer.this.onMessageListener(buffer, false);
			if (TcpServer.this.receiveListener.size() > 0)
			{
				TcpServer.this.receiveListener.post(this, buffer);
			}
		}

		@Override
		public void setParameters(String... parameters)
		{

		}
	}
}
