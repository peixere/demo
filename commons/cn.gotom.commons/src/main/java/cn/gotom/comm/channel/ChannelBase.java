package cn.gotom.comm.channel;

import java.net.PortUnreachableException;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;

import cn.gotom.annotation.Description;
import cn.gotom.commons.Listener;
import cn.gotom.commons.ListenerManager;
import cn.gotom.util.Converter;

/**
 * 通道接口基础实现虚类
 * 
 * @author <a href="mailto:pqixere@qq.com">裴绍国</a>
 * @version 2013-10-16
 */
public abstract class ChannelBase implements Channel
{

	private static final long serialVersionUID = 1L;

	protected final Logger log = Logger.getLogger(this.getClass());

	protected final byte[] receiveBuffer = new byte[1024];

	protected Parameters parameters;

	@Description("接收数据监听器")
	protected final ListenerManager<byte[]> receiveListener = new ListenerManager<byte[]>();

	@Description("状态监听器")
	protected final ListenerManager<State> stateListeners = new ListenerManager<State>();

	@Description("报文监听器")
	protected Listener<String> messageListener;

	protected State state = State.Close;

	protected ChannelBase()
	{
		this.setParameters(new Parameters());
		log.debug("new Channel");
	}

	protected void onState(State state)
	{
		if (this.state != state)
		{
			this.state = state;
			if (stateListeners.size() > 0)
			{
				stateListeners.post(this, state);
			}
		}
	}

	protected void onMessageListener(byte[] buffer, boolean send)
	{
		String hexString = (send ? ">>" : "<<") + Converter.toHexString(buffer);
		if (this.messageListener != null)
		{
			messageListener.onListener(this, hexString);
		}
		else
		{
			log.debug(hexString);
		}
	}

	private long lastTimeMillis = System.currentTimeMillis();

	private int readByteCount = -1;

	protected int receive()
	{
		readByteCount = -1;
		try
		{
			if (connected() && state == State.Connected)
			{
				readByteCount = read(receiveBuffer);
			}
			lastTimeMillis = System.currentTimeMillis();
		}
		catch (PortUnreachableException ex)
		{
			log.debug(Thread.currentThread().getName() + " 通道[" + getId() + "]接收：PortUnreachableException " + ex.getMessage());
		}
		catch (SocketTimeoutException ex)
		{
			if ((System.currentTimeMillis() - lastTimeMillis) > 60000)
			{
				log.debug(Thread.currentThread().getName() + " 通道[" + getId() + "]接收：SocketTimeoutException " + ex.getMessage());
				lastTimeMillis = System.currentTimeMillis();
			}
		}
		catch (java.net.SocketException ex)
		{
			if (State.Close != this.getState())
			{
				if (!ex.getMessage().equalsIgnoreCase("Socket Closed"))
					log.error(Thread.currentThread().getName() + " 通道[" + getId() + "]接收异常：" + ex.getMessage(), ex);
				close();
			}
		}
		catch (Throwable ex)
		{
			if (State.Close != this.getState())
			{
				log.warn(Thread.currentThread().getName() + " 通道[" + getId() + "]接收异常：" + ex.getMessage(), ex);
			}
		}
		try
		{
			if (readByteCount > 0)
			{
				byte[] buffer = new byte[readByteCount];
				System.arraycopy(receiveBuffer, 0, buffer, 0, buffer.length);
				onReceiveListener(buffer);
			}
		}
		catch (Throwable ex)
		{
			log.warn(Thread.currentThread().getName() + " 通道[" + getId() + "]数据处理异常：" + ex.getMessage(), ex);
		}
		return readByteCount;
	}

	protected void onReceiveListener(byte[] buffer)
	{
		onMessageListener(buffer, false);
		if (receiveListener.size() > 0)
		{
			receiveListener.post(this, buffer);
		}
	}

	protected abstract int read(byte[] bytes) throws Exception;

	@Override
	public Listener<String> getMessageListener()
	{
		return messageListener;
	}

	@Override
	public void setMessageListener(Listener<String> messageListener)
	{
		this.messageListener = messageListener;
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

	@Override
	public void removeAllStateListener()
	{
		stateListeners.clear();
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

	@Override
	public void removeAllReceiveListener()
	{
		receiveListener.clear();
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

	@Override
	public State getState()
	{
		return state;
	}

	@Override
	public String getId()
	{
		return this.parameters.getId();
	}

	@Override
	public String toString()
	{
		return parameters.toString();
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
		this.onState(State.Close);
	}
}
