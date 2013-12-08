package cn.gotom.websocket;

import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class Client extends WebSocketClient
{
	protected final Logger log = Logger.getLogger(getClass());

	class KeepTimerTask extends TimerTask
	{
		private final Timer timer = new Timer();

		private final Client client;

		public KeepTimerTask(Client client)
		{
			this.client = client;
		}

		@Override
		public void run()
		{
			try
			{
				if (!closed && !client.getOpen())
				{
					client.connect();
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		public void schedule(long delay, long period)
		{
			timer.schedule(this, delay, period);
		}

		@Override
		public boolean cancel()
		{
			timer.cancel();
			return super.cancel();
		}
	};

	private KeepTimerTask keepTimerTask;
	private boolean open;
	private boolean closed;
	private boolean keepAlive = true;
	private MessageListener<Client, String> messageListener;

	public Client(URI serverURI)
	{
		super(serverURI);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata)
	{
		setOpen(true);
		if (keepTimerTask != null)
		{
			keepTimerTask.cancel();
		}
	}

	@Override
	public void onMessage(String message)
	{
		if (messageListener != null)
		{
			messageListener.onListener(this, message);
		}
		else
		{
			log.debug(message);
		}
	}

	@Override
	public void close()
	{
		closed = true;
		super.close();
	}

	@Override
	public void onClose(int code, String reason, boolean remote)
	{
		if (this.keepAlive && !closed)
		{
			keepTimerTask = new KeepTimerTask(this);
			keepTimerTask.schedule(1000, 1000);
		}
	}

	@Override
	public void onError(Exception ex)
	{
		ex.printStackTrace();
	}

	public boolean getOpen()
	{
		return open;
	}

	private void setOpen(boolean open)
	{
		this.open = open;
	}

	public boolean isKeepAlive()
	{
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive)
	{
		this.keepAlive = keepAlive;
	}

	public MessageListener<Client, String> getMessageListener()
	{
		return messageListener;
	}

	public void setMessageListener(MessageListener<Client, String> messageListener)
	{
		this.messageListener = messageListener;
	}

}
