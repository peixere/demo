package cn.gotom.comm.channel;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChannelManager
{
	public final static ChannelManager instance = new ChannelManager();

	protected Logger log = Logger.getLogger(this.getClass().getName());

	private final ArrayList<Channel> channels = new ArrayList<Channel>();

	private Timer timer;

	public ChannelManager()
	{

	}

	public void start(long delay, long period)
	{
		timer = new Timer();
		TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				for (int i = channels.size() - 1; i >= 0; i--)
				{
					try
					{
						// Channel channel = channels.get(i);
						// channel.receive();
					}
					catch (Throwable e)
					{
						log.log(Level.WARNING, e.getMessage(), e);
					}
				}
			}
		};
		timer.schedule(task, delay, period);
	}

	public synchronized void addChannel(Channel channel)
	{
		channels.add(channel);
	}

	public synchronized void removeChannel(Channel channel)
	{
		channels.remove(channel);
	}

	public synchronized Channel[] getChannels()
	{
		Channel result[] = new Channel[channels.size()];
		result = (Channel[]) channels.toArray(result);
		return result;
	}

	public synchronized void stop()
	{
		timer.cancel();
	}

	public synchronized void clear()
	{
		for (Channel channel : channels)
		{
			channel.close();
		}
		channels.clear();
		System.gc();
	}
}
