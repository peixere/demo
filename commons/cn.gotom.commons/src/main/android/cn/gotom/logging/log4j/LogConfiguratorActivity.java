package cn.gotom.logging.log4j;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import cn.gotom.comm.channel.Channel;
import cn.gotom.comm.channel.TcpChannel;
import cn.gotom.commons.Listener;
import cn.gotom.util.Converter;

public class LogConfiguratorActivity extends Activity
{
	protected final Logger log = Logger.getLogger(LogConfiguratorActivity.class);
	private Channel channel;
	final Listener<byte[]> receiveListener = new Listener<byte[]>()
	{

		@Override
		public void onListener(Object arg0, byte[] bytes)
		{
			log.error(Converter.toHexString(bytes));
		}
	};

	protected void sendOnClick(View v)
	{
		try
		{
			log.error("channel.connect()");
			channel.connect();
			channel.write(new byte[] { 0x01, 0x03, 0x02, 0x11, 0x00, 0x06, (byte) 0x94, 0x75 });

		}
		catch (Exception e)
		{
			log.error(e.getMessage());
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		final LogConfigurator logConfigurator = new LogConfigurator();
		String filename = Environment.getExternalStorageDirectory() + File.separator + "log.log";
		logConfigurator.setFileName(filename);
		logConfigurator.setRootLevel(Level.DEBUG);
		logConfigurator.setLevel("org.apache", Level.ERROR);
		logConfigurator.configure();
		log.debug(filename);

		// setContentView(R.layout.activity_main);
		channel = new TcpChannel("192.168.0.110", 4001);
		channel.addReceiveListener(receiveListener);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		try
		{
			log.debug("onDestroy");
			if (channel != null)
			{
				channel.close();
			}
		}
		catch (Exception ex)
		{
			log.error(ex.getMessage());
		}
	}
}
