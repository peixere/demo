package cn.gotom.comm.channel;

import java.io.IOException;

import org.apache.log4j.Logger;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;
import cn.gotom.annotation.Description;
import cn.gotom.util.ClassLoaderUtils;
import cn.gotom.util.FileUtils;
import cn.gotom.util.SystemType;

@Description("串口")
@ChannelType(ChannelTypeEnum.SerialPort)
public class SerialPortChannel extends ChannelBase implements SerialPortEventListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(SerialPortChannel.class);

	private SerialPortJssc sPort;
	static
	{
		if (SystemType.Android == SystemType.current())
		{
			log.error("Unsupported on " + System.getProperty("os.name"));
			throw new UnsupportedOperationException("this os Unsupported");
		}
		String javaHome = System.getProperty("java.home");
		String fromfile = null;
		String tofile = null;
		fromfile = ClassLoaderUtils.getPath(SerialPort.class);
		tofile = javaHome + "/lib/ext/jssc.jar";
		FileUtils.copy(fromfile, tofile);
		log.info("load Native Jssc Library");
	}

	public SerialPortChannel()
	{
		this(new Parameters());
	}

	public SerialPortChannel(Parameters parameters)
	{
		super();
		setParameters(parameters);
	}

	@Override
	public synchronized void close()
	{
		try
		{
			removeEventListener();
			if (sPort != null && sPort.isOpened())
			{
				sPort.closePort();
				log.info("closed[" + getId() + "]");
			}
		}
		catch (SerialPortException e)
		{
			log.error("closed[" + getId() + "]" + e.getMessage());
		}
		sPort = null;
		super.close();
	}

	@Override
	public synchronized void connect() throws IOException
	{
		try
		{
			if (!connected())
			{
				removeEventListener();
				sPort = new SerialPortJssc(parameters.getPortName());
				this.onState(State.Connecting);
				sPort.openPort();
				sPort.setParams(parameters.getBaudRate(), parameters.getDatabits(), parameters.getStopbits(), parameters.getParity());
				sPort.setFlowControlMode(parameters.getFlowControlIn() | parameters.getFlowControlOut());
				int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
				sPort.setEventsMask(mask);
				sPort.addEventListener(this);
				log.info("连接成功[" + this.getId() + "]");
				this.onState(State.Connected);
			}
		}
		catch (Exception ex)
		{
			this.close();
			this.onState(State.Fail);
			log.error(Thread.currentThread().getName() + "通道[" + getId() + "]连接异常：" + ex.getMessage(), ex);
			throw new IOException(ex);
		}
	}

	private void removeEventListener()
	{
		try
		{
			if (sPort != null)
				sPort.removeEventListener();
		}
		catch (SerialPortException e)
		{
			log.error("通道[" + getId() + "]异常：" + e.getMessage());
		}
	}

	@Override
	public void setParameters(Parameters parameters)
	{
		super.setParameters(parameters);
		this.parameters.setChannelType(ChannelTypeEnum.SerialPort);
	}

	// @Override
	// public void setParameters(String... params)
	// {
	// String portName = params[0];
	// int baudRate = Converter.parseInt(params[1]);
	// int flowControlIn = Converter.parseInt(params[2]);
	// int flowControlOut = Converter.parseInt(params[3]);
	// int databits = Converter.parseInt(params[4]);
	// int stopbits = Converter.parseInt(params[5]);
	// int parity = Converter.parseInt(params[6]);
	// parameters.setPortName(portName);
	// if (baudRate > 0)
	// parameters.setBaudRate(baudRate);
	// if (flowControlIn > 0)
	// parameters.setFlowControlIn(flowControlIn);
	// if (flowControlOut > 0)
	// parameters.setFlowControlOut(flowControlOut);
	// if (flowControlOut > 0)
	// parameters.setDatabits(databits);
	// if (stopbits > 0)
	// parameters.setStopbits(stopbits);
	// if (parity > 0)
	// parameters.setParity(parity);
	// }

	@Override
	public String getId()
	{
		return this.parameters.getId();
	}

	@Override
	public boolean connected()
	{
		return sPort != null && sPort.isOpened();
	}

	@Override
	public void serialEvent(SerialPortEvent e)
	{
		if (e.isRXCHAR())
		{
			receive();
		}
	}

	@Override
	protected int read(byte[] bytes) throws Exception
	{
		byte[] rBtyes = sPort.readBytes();
		if (rBtyes != null && rBtyes.length > 0)
		{
			System.arraycopy(rBtyes, 0, bytes, 0, rBtyes.length);
			return rBtyes.length;
		}
		return -1;
	}

	@Override
	public void write(byte[] bytes) throws IOException
	{
		try
		{
			sPort.writeBytes(bytes);
			onMessageListener(bytes, true);
		}
		catch (SerialPortException ex)
		{
			log.error(" 通道[" + getId() + "]发送异常：" + ex.getMessage(), ex);
			this.close();
			throw new IOException(ex.getMessage(), ex);
		}
	}

	public static String[] listPort()
	{
		return SerialPortList.getPortNames();
	}

	private class SerialPortJssc extends SerialPort
	{
		public SerialPortJssc(String portName)
		{
			super(portName);
		}

		@Override
		public boolean writeBytes(byte[] buffer) throws SerialPortException
		{
			if (!checkPortOpened(this.getPortName()))
			{
				try
				{
					this.closePort();
				}
				catch (SerialPortException e)
				{
					log.error(e.getMessage());
				}
				throw new SerialPortException(getPortName(), "writeBytes()", SerialPortException.TYPE_PORT_NOT_FOUND);
			}
			return super.writeBytes(buffer);
		}

		private boolean checkPortOpened(String portName)
		{
			if (this.isOpened())
			{
				String[] portNames = SerialPortList.getPortNames();
				for (int i = 0; i < portNames.length; i++)
				{
					if (portNames[i].equalsIgnoreCase(portName))
					{
						return true;
					}
				}
			}
			return false;
		}
	}
}
