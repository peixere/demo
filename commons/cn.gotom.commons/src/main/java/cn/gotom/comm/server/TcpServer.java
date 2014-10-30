package cn.gotom.comm.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.gotom.annotation.Description;
import cn.gotom.comm.channel.Channel;
import cn.gotom.comm.channel.ChannelBase;
import cn.gotom.comm.channel.ChannelImpl;
import cn.gotom.comm.channel.ChannelType;
import cn.gotom.comm.channel.ChannelTypeEnum;
import cn.gotom.comm.channel.Parameters;
import cn.gotom.comm.channel.State;
import cn.gotom.util.GList;

@Description("TCP服务端")
@ChannelType(ChannelTypeEnum.TCPServer)
public class TcpServer extends ChannelBase implements Server
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ServerSocket socket;

	private Timer terminalTimer;
	private GList<Terminal> terminalList = new GList<Terminal>();
	public static final ThreadLocal<Channel> terminalPool = new ThreadLocal<Channel>();

	private int maxTerminalSize = 1024;

	public TcpServer()
	{
		this("0.0.0.0", 40000);
	}

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
				socket.bind(new InetSocketAddress(parameters.getAddress(), parameters.getPort()));
				log.info("启动成功[" + this.getId() + "]SoTimeout=" + socket.getSoTimeout());
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
			log.warn(Thread.currentThread().getName() + "通道[" + getId() + "]启动异常：" + ex.getClass().getName() + ": " + ex.getMessage());
			throw ex;
		}
	}

	@Override
	protected int receive()
	{
		return -1;
	}

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
				checkConnection();
			}

		};
		terminalTimer.schedule(task, 1000, 1);
	}

	private void checkConnection()
	{
		try
		{
			for (int i = terminalList.size() - 1; i >= 0; i--)
			{
				Terminal terminal = terminalList.get(i);
				//terminal.write(new byte[]{-1,-1});
				if (!terminal.connected())
				{
					terminalList.remove(i);
				}
			}
		}
		catch (Throwable e)
		{
		}
	}

	private void receiveTerminal()
	{
		if (connected())
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
				log.info("terminal count : " + terminalList.size());
			}
			catch (Throwable e)
			{
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
			for (int i = terminalList.size() - 1; i >= 0; i--)
			{
				Channel chennel = terminalList.get(i);
				chennel.close();
			}
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
		// Channel terminal = terminalPool.get();
		// if (terminal != null)
		// {
		// terminal.write(bytes);
		// }
		// List<Channel> channelList = getClientList();
		// for (Channel c : channelList)
		for (Channel c : this.terminalList)
		{
			try
			{
				if (c.connected())
					c.write(bytes);
			}
			catch (Throwable e)
			{
				log.error(e.getClass().getName() + ": " + e.getMessage(), e);
			}
		}
		// while(this.terminalList.get(location))
	}

	@Override
	protected int read(byte[] bytes) throws Exception
	{
		return -1;
	}

	@Override
	public boolean started()
	{
		return this.connected();
	}

	@Override
	public void start() throws IOException
	{
		this.connect();
	}

	@Override
	public void stop()
	{
		this.close();
	}

	@Override
	public List<Channel> getClientList()
	{
		GList<Channel> tmpList = new GList<Channel>();
		if (terminalList.size() > 0)
			tmpList.AddRange(terminalList.asArray());
		return tmpList;
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
			this.parameters.setAddress(socket.getInetAddress().getHostAddress());
			this.parameters.setPort(socket.getPort());
			this.parameters.setLocalPort(socket.getLocalPort());
			this.parameters.setChannelType(ChannelTypeEnum.TCPServer);
			this.connect();
		}

		@Override
		public void connect()
		{
			try
			{
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
				socket.setSoTimeout(1000);
				log.info("连接成功[" + this.getId() + "]SoTimeout=" + socket.getSoTimeout());
				this.onState(State.Connected);
				super.connect();
				if (TcpServer.this.stateListeners.size() > 0)
				{
					TcpServer.this.stateListeners.post(this, State.Connected);
				}
			}
			catch (Throwable e)
			{
				log.error(e.getClass().getName() + ": " + e.getMessage());
			}
		}

		@Override
		public boolean connected()
		{
			return socket != null && socket.isConnected() && !socket.isClosed();
		}

		@Override
		public void write(byte[] bytes) throws IOException
		{
			try
			{
				super.write(bytes);
			}
			catch (java.net.SocketException ex)
			{
				log.warn(" 通道[" + getId() + "]发送异常：" + ex.getMessage());
				this.close();
				throw new IOException(ex.getMessage(), ex);
			}
			catch (Throwable ex)
			{
				log.warn(Thread.currentThread().getName() + " close[" + this.getId() + "]", ex);
			}
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
				log.info("terminal count : " + terminalList.size());
				if (socket != null)
				{
					if (socket.isConnected())
						log.info("closed[" + this.getId() + "]");
					socket.shutdownInput();
					socket.shutdownOutput();
					socket.close();
				}
				socket = null;
			}
			catch (Throwable ex)
			{
				log.warn(Thread.currentThread().getName() + " close[" + this.getId() + "]", ex);
			}
			super.close();
			if (TcpServer.this.stateListeners.size() > 0)
			{
				TcpServer.this.stateListeners.post(this, State.Close);
			}
		}

		@Override
		protected void onReceiveListener(byte[] buffer)
		{
			TcpServer.this.onMessageListener(buffer, false);
			if (TcpServer.this.receiveListener.size() > 0)
			{
				TcpServer.this.receiveListener.post(this, buffer);
			}
		}

		@Override
		public void setParameters(Parameters parameters)
		{
			super.setParameters(parameters);
			this.parameters.setChannelType(ChannelTypeEnum.TCPServer);
		}
	}

}
