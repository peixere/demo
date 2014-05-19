package cn.gotom.comm.channel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

import org.apache.log4j.Logger;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import cn.gotom.annotation.Description;

@Description("蓝牙通道实现")
@ChannelType(ChannelTypeEnum.Bluetooth)
public class BlueChannel extends ChannelImpl
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final Logger log = Logger.getLogger(BlueChannel.class);
	public final static int sdk = Integer.parseInt(Build.VERSION.SDK);
	public final static UUID SerialPort = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	public static final int REQUEST_CONNECT_DEVICE = 1;
	public static final int REQUEST_ENABLE = 2;
	private BluetoothAdapter blueAdapter;

	private BluetoothDevice device;
	private BluetoothSocket socket;
	private int maxConnectCount = 1;

	public BlueChannel(String blueAddress)
	{
		super();
		if (blueAdapter == null)
			blueAdapter = BluetoothAdapter.getDefaultAdapter();
		parameters.setBluePort(blueAddress);
		setParameters(parameters);
		device = getRemoteDevice();
	}

	@Override
	public void setParameters(Parameters parameters)
	{
		this.parameters = parameters;
		this.parameters.setChannelType(ChannelTypeEnum.Bluetooth);
	}

	@Override
	public synchronized void connect() throws IOException
	{
		try
		{
			blueAdapter.cancelDiscovery();
			if (connected())
				return;
			// setPin("1234");
			if (sdk >= 10)
			{
				socket = device.createInsecureRfcommSocketToServiceRecord(SerialPort);
			}
			else
			{
				socket = device.createRfcommSocketToServiceRecord(SerialPort);
			}
			// log.debug("[" + blueAddress + "]正在连接蓝牙...");
			this.onState(State.Connecting);
			this.socket.connect();
			log.debug("[" + this.getId() + "]连接蓝牙成功");
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			this.onState(State.Connected);
			super.connect();
		}
		catch (IOException ex)
		{
			log.error("[" + this.getId() + "]连接蓝牙异常：" + ex.getClass().getName() + " " + ex.getMessage());
			ex.printStackTrace();
			maxConnectCount--;
			if (maxConnectCount == 0)
			{
				close();
			}
			this.onState(State.Fail);
			throw ex;
		}
	}

	@Override
	public String getId()
	{
		return this.parameters.getId();
	}

	@Override
	public boolean connected()
	{
		return socket != null && this.getState() == State.Connected;
	}

	@Override
	public void close()
	{

		try
		{
			if (socket != null)
			{
				socket.close();
				log.debug("closed[" + getId() + "]");
			}
			socket = null;
		}
		catch (IOException e)
		{
			log.debug("close[" + getId() + "]", e);
		}
		super.close();
	}

	private String getAddress()
	{
		return this.getParameters().getBluePort();
	}

	public BluetoothDevice getRemoteDevice()
	{
		if (device == null)
		{
			if (BluetoothAdapter.checkBluetoothAddress(getAddress()))
			{
				device = blueAdapter.getRemoteDevice(getAddress());
			}
			else
			{
				log.debug("[" + getAddress() + "]蓝牙地址不正确");
			}
		}
		return device;
	}

	public static boolean enable()
	{
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter != null)
		{
			log.debug("蓝牙设备已经启用");
			adapter.disable();
			return adapter.enable();
		}
		else
		{
			log.debug("找不到可用的蓝牙设备");
			return false;
		}
	}

	public static boolean disable()
	{
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter != null)
		{
			return adapter.disable();
		}
		else
		{
			log.debug("找不到可用的蓝牙设备");
			return false;
		}
	}

	public boolean setPin(String password)
	{
		if (enable() && getRemoteDevice() != null)
		{
			if (device.getBondState() != BluetoothDevice.BOND_BONDED)
			{
				try
				{
					setPin(device, password);
					createBond(device);
					return true;
				}
				catch (Exception e)
				{
					log.debug("setPiN failed!", e);
					return false;
				}
			}
			else
			{
				return true;
			}
		}
		else
		{
			return false;
		}
	}

	/**
	 * 建立配对
	 * 
	 * @param btDevice
	 * @return
	 * @throws Exception
	 */
	public static boolean createBond(BluetoothDevice btDevice) throws Exception
	{
		Method createBondMethod = btDevice.getClass().getMethod("createBond");
		Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
		return returnValue.booleanValue();
	}

	/**
	 * 删除配对
	 * 
	 * @param btDevice
	 * @return
	 * @throws Exception
	 */
	static public boolean removeBond(BluetoothDevice btDevice) throws Exception
	{
		Method removeBondMethod = btDevice.getClass().getMethod("removeBond");
		Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
		return returnValue.booleanValue();
	}

	/**
	 * 自动配对密码
	 * 
	 * @param btDevice
	 * @param str
	 * @return
	 * @throws Exception
	 */
	static public boolean setPin(BluetoothDevice btDevice, String str) throws Exception
	{
		try
		{
			Method removeBondMethod = btDevice.getClass().getDeclaredMethod("setPin", new Class[] { byte[].class });
			Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice, new Object[] { str.getBytes() });
			log.debug(btDevice.getAddress() + "setPin : " + returnValue);
			return returnValue;
		}
		catch (SecurityException e)
		{
			log.debug("setPiN failed!", e);
		}
		catch (IllegalArgumentException e)
		{
			log.debug("setPiN failed!", e);
		}
		catch (Exception e)
		{
			log.debug("setPiN failed!", e);
		}
		return false;
	}
}
