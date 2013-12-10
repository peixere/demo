package cn.gotom.sso.websocket;

import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class WSClient extends WebSocketClient
{
	protected final Logger log = Logger.getLogger(getClass());

	class KeepTimerTask extends TimerTask
	{
		private final Timer timer = new Timer();

		private final WSClient client;

		public KeepTimerTask(WSClient client)
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
	private int periodMinutes = 3000;
	private Listener<WSClient, String> receiveListener;

	public WSClient(URI serverURI)
	{
		super(serverURI);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata)
	{
		// this.send("");
		setOpen(true);
		if (keepTimerTask != null)
		{
			keepTimerTask.cancel();
		}
	}

	@Override
	public void onMessage(String message)
	{
		if (receiveListener != null)
		{
			receiveListener.onListener(this, message);
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
			keepTimerTask.schedule(3000, periodMinutes);
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

	public Listener<WSClient, String> getReceiveListener()
	{
		return receiveListener;
	}

	public void setReceiveListener(Listener<WSClient, String> receiveListener)
	{
		this.receiveListener = receiveListener;
	}

	public int getPeriodMinutes()
	{
		return periodMinutes;
	}

	public void setPeriodMinutes(int periodMinutes)
	{
		this.periodMinutes = periodMinutes;
	}

}
