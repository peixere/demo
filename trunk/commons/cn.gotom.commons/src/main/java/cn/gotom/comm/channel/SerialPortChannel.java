package cn.gotom.comm.channel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;
import cn.gotom.annotation.Description;
import cn.gotom.util.ClassLoaderUtils;
import cn.gotom.util.Converter;
import cn.gotom.util.FileUtils;

@Description("串口")
@ChannelType(ChannelTypeEnum.SerialPort)
public class SerialPortChannel extends ChannelBase implements SerialPortEventListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SerialPort sPort;

	public SerialPortChannel()
	{
		super();
		initJni();
	}

	private void initJni()
	{
		log.info("os.name=" + System.getProperty("os.name"));
		log.info("os.arch=" + System.getProperty("os.arch"));
		log.info("java.home=" + System.getProperty("java.home"));
		// SystemType.current();
		String javaHome = System.getProperty("java.home");
		String fromfile = null;
		String tofile = null;
		fromfile = ClassLoaderUtils.getPath(SerialPort.class);
		tofile = javaHome + "/lib/ext/jssc.jar";
		FileUtils.cover(fromfile, tofile);
	}

	public SerialPortChannel(Parameters parameters)
	{
		super();
		this.parameters = parameters;
	}

	@Override
	public synchronized void close()
	{
		try
		{
			removeEventListener();
			if (sPort != null)
			{
				sPort.closePort();
				sPort = null;
				log.info("closed[" + getId() + "]");
			}
		}
		catch (SerialPortException e)
		{
			log.error("closed[" + getId() + "]" + e.getMessage());
		}
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
				sPort = new SerialPort(parameters.getPortName());
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
	public void setParameters(String... params)
	{
		String portName = params[0];
		int baudRate = Converter.parseInt(params[1]);
		int flowControlIn = Converter.parseInt(params[2]);
		int flowControlOut = Converter.parseInt(params[3]);
		int databits = Converter.parseInt(params[4]);
		int stopbits = Converter.parseInt(params[5]);
		int parity = Converter.parseInt(params[6]);
		parameters.setPortName(portName);
		if (baudRate > 0)
			parameters.setBaudRate(baudRate);
		if (flowControlIn > 0)
			parameters.setFlowControlIn(flowControlIn);
		if (flowControlOut > 0)
			parameters.setFlowControlOut(flowControlOut);
		if (flowControlOut > 0)
			parameters.setDatabits(databits);
		if (stopbits > 0)
			parameters.setStopbits(stopbits);
		if (parity > 0)
			parameters.setParity(parity);
	}

	@Override
	public String getId()
	{
		return this.parameters.toSerialString();
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
			onMessageListener(bytes, true);
			sPort.writeBytes(bytes);
		}
		catch (SerialPortException e)
		{
			throw new IOException(e.getMessage(), e);
		}
	}

	public static List<String> listPort()
	{
		List<String> listPort = new ArrayList<String>();
		String[] portNames = SerialPortList.getPortNames();
		for (int i = 0; i < portNames.length; i++)
		{
			listPort.add(portNames[i]);
		}
		return listPort;
	}
}
