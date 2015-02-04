package cn.gotom.comm.demo;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.gotom.comm.channel.Channel;
import cn.gotom.comm.channel.Parameters;
import cn.gotom.comm.channel.State;
import cn.gotom.comm.channel.TcpChannel;
import cn.gotom.commons.Listener;
import cn.gotom.logging.log4j.LogConfigurator;
import cn.gotom.util.Converter;

public class MainActivity extends Activity {
	protected final Logger log = Logger.getLogger(MainActivity.class);
	private final LogConfigurator logConfigurator = new LogConfigurator();
	private Channel channel;
	private final Handler refresh = new Handler();
	final Listener<byte[]> receiveListener = new Listener<byte[]>() {

		@Override
		public void onListener(Object arg0, final byte[] bytes) {
			refresh.post(new Runnable() {
				@Override
				public void run() {
					try {
						EditText textView = (EditText) findViewById(R.id.editTextReceive);
						String text = textView.getText().toString();
						text += Converter.toHexString(bytes);
						textView.setText(text);
						openGPSSettings();
						getLocation();
					} catch (Exception ex) {
						log.error(ex.getMessage());
						ex.printStackTrace();
					}
				}
			});
		}
	};

	final Listener<State> stateListener = new Listener<State>() {

		@Override
		public void onListener(Object arg0, State state) {
			onStateListener(arg0, state);
		}
	};

	public MainActivity() {
		super();
		Log.d("MainActivity", "new MainActivity()");
	}

	public void onStateListener(Object arg0, State state) {
		log.debug(state);
		Button conn = (Button) this.findViewById(R.id.buttonConn);
		if (state.ordinal() > 1) {
			conn.setText(R.string.buttonConn);
		} else {
			conn.setText(R.string.buttonClose);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		logConfigure();
		if (channel == null) {
			channel = new TcpChannel("192.168.0.120", 4001);
			channel.setReceiveListener(receiveListener);
			channel.setStateListener(stateListener);
		}
		// this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		log.debug("onCreate");
		final Button conn = (Button) this.findViewById(R.id.buttonConn);
		conn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				connOnClick(v);
			}
		});

		Button buttonSend = (Button) this.findViewById(R.id.buttonSend);
		buttonSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendOnClick(v);
			}
		});

		EditText sendTextView = (EditText) this.findViewById(R.id.editTextSend);
		byte[] buffer = new byte[] { 0x01, 0x03, 0x02, 0x11, 0x00, 0x06,
				(byte) 0x94, 0x75 };
		sendTextView.setText(Converter.toHexString(buffer));
	}

	private void openGPSSettings() {
		LocationManager alm = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			Toast.makeText(this, "GPS模块正常", Toast.LENGTH_SHORT).show();
			return;
		}

		Toast.makeText(this, "请开启GPS！", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
		startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面

	}

	private void getLocation() {
		// 获取位置管理服务
		LocationManager locationManager;
		String serviceName = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) this.getSystemService(serviceName);
		// 查找到服务信息
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗

		String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
		Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
		updateToNewLocation(location);
		// 设置监听器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
		locationManager.requestLocationUpdates(provider, 100 * 1000, 500,
				new LocationListener() {

					@Override
					public void onLocationChanged(Location location) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProviderDisabled(String provider) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProviderEnabled(String provider) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStatusChanged(String provider, int status,
							Bundle extras) {
						// TODO Auto-generated method stub

					}
				});
	}

	private void updateToNewLocation(Location location) {

		EditText textView = (EditText) findViewById(R.id.editTextReceive);
		if (location != null) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			textView.setText("维度：" + latitude + "\n经度" + longitude);
		} else {
			textView.setText("无法获取地理信息");
		}
	}

	private void logConfigure() {
		try {
			final String filename = Environment.getExternalStorageDirectory()
					+ File.separator + this.getPackageName() + ".log";
			logConfigurator.setFileName(filename);
			logConfigurator.setRootLevel(Level.DEBUG);
			logConfigurator.setLevel("org.apache", Level.ERROR);
			logConfigurator.configure();
		} catch (Exception ex) {
			final String filename = getApplicationContext().getFilesDir()
					.getAbsolutePath()
					+ File.separator
					+ this.getPackageName()
					+ ".log";
			logConfigurator.setFileName(filename);
			logConfigurator.setRootLevel(Level.DEBUG);
			logConfigurator.setLevel("org.apache", Level.ERROR);
			logConfigurator.configure();
			ex.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			log.debug("onDestroy");
			if (channel != null) {
				channel.close();
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}

	private void connOnClick(View v) {
		try {
			Button conn = (Button) v;
			if (conn.getText().equals(
					getResources().getString(R.string.buttonConn))) {
				conn.setText(R.string.buttonClose);
				if (channel != null) {
					channel.close();
				}
				log.debug("channel.connect()");
				EditText addressView = (EditText) this
						.findViewById(R.id.textAddress);
				String address = addressView.getText().toString();
				EditText portView = (EditText) this.findViewById(R.id.textPort);
				int port = Integer.parseInt(portView.getText().toString());
				channel.setParameters(new Parameters(address, port));
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							channel.connect();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			} else {
				conn.setText(R.string.buttonConn);
				if (channel != null) {
					channel.close();
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void sendOnClick(View v) {
		try {
			EditText sendTextView = (EditText) this
					.findViewById(R.id.editTextSend);
			byte[] buffer = new byte[] { 0x01, 0x03, 0x02, 0x11, 0x00, 0x06,
					(byte) 0x94, 0x75 };
			buffer = Converter.toBytes(sendTextView.getText().toString());
			final byte[] sendBuffer = buffer;
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						channel.write(sendBuffer);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
	}
}
