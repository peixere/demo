package cn.gotom.comm.channel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.PortUnreachableException;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import cn.gotom.annotation.Description;
import cn.gotom.commons.Listener;
import cn.gotom.commons.ListenerManager;
import cn.gotom.util.Converter;

public abstract class ChannelImpl implements Channel
{

	private static final long serialVersionUID = 1L;

	protected final byte[] receiveBuffer = new byte[1024];

	protected final Logger log = Logger.getLogger(this.getClass());

	protected DataInputStream in;

	protected DataOutputStream out;

	protected Parameters parameters;

	@Description("接收数据监听器")
	protected final ListenerManager<byte[]> receiveListener = new ListenerManager<byte[]>();

	protected final ListenerManager<State> stateListeners = new ListenerManager<State>();

	protected State state = State.Close;

	private Timer receiveTimer;

	protected ChannelImpl()
	{
		this.setParameters(new Parameters());
		log.debug("new ChannelImpl()");
	}

	@Override
	public State getState()
	{
		return state;
	}

	@Override
	public void addStateListener(Listener<State> stateListener)
	{
		stateListeners.add(stateListener);
	}

	@Override
	public void removeStateListener(Listener<State> stateListener)
	{
		stateListeners.remove(stateListener);
	}

	protected void onState(State state)
	{
		this.state = state;
		if (stateListeners != null)
		{
			stateListeners.post(this, state);
		}
	}

	@Override
	public void connect() throws IOException
	{
		if (receiveTimer != null)
		{
			receiveTimer.cancel();
		}
		receiveTimer = new Timer("Timer  Receive");
		TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				receive();
			}
		};
		receiveTimer.schedule(task, 1000, 1);
	}

	protected void receive()
	{
		try
		{
			if (connected() && state == State.Connected)
			{
				int readBytes = read(receiveBuffer);
				if (readBytes > 0)
				{
					byte[] buffer = new byte[readBytes];
					System.arraycopy(receiveBuffer, 0, buffer, 0, buffer.length);
					if (receiveListener != null)
					{
						log.debug(Converter.toHexString(buffer));
						receiveListener.post(this, buffer);
					}
					else
					{
						log.warn(Converter.toHexString(buffer));
					}
				}
			}
		}
		catch (PortUnreachableException ex)
		{
			// UDP目标未打开
			// log.info(Thread.currentThread().getName() + " 通道[" + getId() + "]接收：PortUnreachableException " + ex.getMessage());
		}
		catch (SocketTimeoutException ex)
		{
			// log.info(Thread.currentThread().getName() + " 通道[" + getId() + "]接收：SocketTimeoutException " + ex.getMessage());
		}
		catch (java.net.SocketException ex)
		{
			if (receiveTimer != null)
			{
				log.error(Thread.currentThread().getName() + " 通道[" + getId() + "]接收异常：" + ex.getMessage(), ex);
				close();
			}
		}
		catch (Throwable ex)
		{
			if (receiveTimer != null)
			{
				log.warn(Thread.currentThread().getName() + " 通道[" + getId() + "]接收异常：" + ex.getMessage(), ex);
			}
		}
	}

	@Description("设置接收数据监听器")
	@Override
	public void addReceiveListener(Listener<byte[]> listener)
	{
		receiveListener.add(listener);
	}

	@Description("删除接收数据监听器")
	@Override
	public void removeReceiveListener(Listener<byte[]> listener)
	{
		receiveListener.remove(listener);
	}

	protected int read(byte[] bytes) throws IOException
	{
		return in.read(bytes);
	}

	@Override
	public void write(byte[] bytes) throws IOException
	{
		out.write(bytes);
	}

	@Override
	public String toString()
	{
		return getId();
	}

	@Override
	public boolean equals(Object object)
	{
		if (object == null)
			return false;
		return this.toString().equals(object.toString());
	}

	@Override
	public void close()
	{
		try
		{
			if (receiveTimer != null)
			{
				receiveTimer.cancel();
			}
			receiveTimer = null;
			if (in != null)
			{
				in.close();
			}
			if (out != null)
			{
				out.close();
			}
			in = null;
			out = null;
			this.onState(State.Close);
		}
		catch (Exception ex)
		{
			log.warn("close[" + this.getId() + "]", ex);
		}
	}

	@Override
	public Parameters getParameters()
	{
		return parameters;
	}

	@Override
	public void setParameters(Parameters parameters)
	{
		this.parameters = parameters;
	}
}
