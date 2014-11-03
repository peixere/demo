package cn.gotom.comm.demo;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.gotom.comm.channel.Channel;
import cn.gotom.comm.channel.Parameters;
import cn.gotom.comm.channel.State;
import cn.gotom.comm.channel.TcpChannel;
import cn.gotom.commons.Listener;
import cn.gotom.logging.log4j.LogConfigurator;
import cn.gotom.util.Converter;

public class MainActivity extends Activity
{
	protected final Logger log = Logger.getLogger(MainActivity.class);
	private final LogConfigurator logConfigurator = new LogConfigurator();
	private Channel channel;
	private final Handler refresh = new Handler();
	final Listener<byte[]> receiveListener = new Listener<byte[]>()
	{

		@Override
		public void onListener(Object arg0, final byte[] bytes)
		{
			refresh.post(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						EditText textView = (EditText) findViewById(R.id.editTextReceive);
						String text = textView.getText().toString();
						text += Converter.toHexString(bytes);
						textView.setText(text);
					}
					catch (Exception ex)
					{
						log.error(ex.getMessage());
						ex.printStackTrace();
					}
				}
			});
		}
	};

	final Listener<State> stateListener = new Listener<State>()
	{

		@Override
		public void onListener(Object arg0, State state)
		{
			onStateListener(arg0, state);
		}
	};

	public MainActivity()
	{
		super();
		Log.d("MainActivity", "new MainActivity()");
	}

	public void onStateListener(Object arg0, State state)
	{
		log.debug(state);
		Button conn = (Button) this.findViewById(R.id.buttonConn);
		if (state.ordinal() > 1)
		{
			conn.setText(R.string.buttonConn);
		}
		else
		{
			conn.setText(R.string.buttonClose);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		logConfigure();
		if (channel == null)
		{
			channel = new TcpChannel("192.168.0.120", 4001);
			channel.setReceiveListener(receiveListener);
			channel.setStateListener(stateListener);
		}
		// this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		log.debug("onCreate");
		final Button conn = (Button) this.findViewById(R.id.buttonConn);
		conn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				connOnClick(v);
			}
		});

		Button buttonSend = (Button) this.findViewById(R.id.buttonSend);
		buttonSend.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				sendOnClick(v);
			}
		});

		EditText sendTextView = (EditText) this.findViewById(R.id.editTextSend);
		byte[] buffer = new byte[] { 0x01, 0x03, 0x02, 0x11, 0x00, 0x06, (byte) 0x94, 0x75 };
		sendTextView.setText(Converter.toHexString(buffer));
	}

	private void logConfigure()
	{
		try
		{
			final String filename = Environment.getExternalStorageDirectory() + File.separator + this.getPackageName() + ".log";
			logConfigurator.setFileName(filename);
			logConfigurator.setRootLevel(Level.DEBUG);
			logConfigurator.setLevel("org.apache", Level.ERROR);
			logConfigurator.configure();
		}
		catch (Exception ex)
		{
			final String filename = getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + this.getPackageName() + ".log";
			logConfigurator.setFileName(filename);
			logConfigurator.setRootLevel(Level.DEBUG);
			logConfigurator.setLevel("org.apache", Level.ERROR);
			logConfigurator.configure();
			ex.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
			log.error(ex.getMessage(), ex);
		}
	}

	private void connOnClick(View v)
	{
		try
		{
			Button conn = (Button) v;
			if (conn.getText().equals(getResources().getString(R.string.buttonConn)))
			{
				conn.setText(R.string.buttonClose);
				if (channel != null)
				{
					channel.close();
				}
				log.debug("channel.connect()");
				EditText addressView = (EditText) this.findViewById(R.id.textAddress);
				String address = addressView.getText().toString();
				EditText portView = (EditText) this.findViewById(R.id.textPort);
				int port = Integer.parseInt(portView.getText().toString());
				channel.setParameters(new Parameters(address, port));
				new Thread(new Runnable()
				{

					@Override
					public void run()
					{
						try
						{
							channel.connect();
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}).start();
			}
			else
			{
				conn.setText(R.string.buttonConn);
				if (channel != null)
				{
					channel.close();
				}
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
	}

	private void sendOnClick(View v)
	{
		try
		{
			EditText sendTextView = (EditText) this.findViewById(R.id.editTextSend);
			byte[] buffer = new byte[] { 0x01, 0x03, 0x02, 0x11, 0x00, 0x06, (byte) 0x94, 0x75 };
			buffer = Converter.toBytes(sendTextView.getText().toString());
			final byte[] sendBuffer = buffer;
			new Thread(new Runnable()
			{

				@Override
				public void run()
				{
					try
					{
						channel.write(sendBuffer);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}).start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
	}
}
