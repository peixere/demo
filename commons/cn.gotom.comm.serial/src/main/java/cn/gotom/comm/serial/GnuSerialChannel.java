package cn.gotom.comm.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.CommPortOwnershipListener;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;

import cn.gotom.comm.channel.ChannelImpl;
import cn.gotom.comm.channel.State;
import cn.gotom.util.SystemType;

abstract class GnuSerialChannel extends ChannelImpl implements CommPortOwnershipListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CommPortIdentifier portId;
	private SerialPort sPort;
	protected boolean connected = false;
	static
	{
		try
		{
			SerialPortLibrary.init();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void close()
	{
		super.close();
		connected = false;
		if (sPort != null)
		{
			sPort.removeEventListener();
			sPort.close();
			log.debug("closed[" + getId() + "]");
			portId.removePortOwnershipListener(this);
		}
		sPort = null;
	}

	@Override
	public void connect() throws IOException
	{
		if (connected)
			return;
		try
		{
			this.onState(State.Connecting);
			connectGun();
			this.onState(State.Connected);
		}
		catch (IOException ex)
		{
			this.close();
			this.onState(State.Fail);
			log.error(Thread.currentThread().getName() + "connect to [" + getId() + "] fail : " + ex.getMessage(), ex);
			throw ex;
		}
	}

	private synchronized void connectGun() throws IOException
	{
		try
		{
			if (sPort != null)
			{
				sPort.removeEventListener();
				portId.removePortOwnershipListener(this);
				sPort.close();
			}
			portId = CommPortIdentifier.getPortIdentifier(parameters.getPortName());
			sPort = (SerialPort) portId.open(parameters.getPortName(), 3000);
			setConnectionParameters();
		}
		catch (NoSuchPortException e)
		{
			throw new IOException("no such port [" + getId() + "]" + e.getMessage());
		}
		catch (PortInUseException e)
		{
			throw new IOException("port in use [" + getId() + "]" + e.getMessage());
		}
		catch (UnsupportedCommOperationException e)
		{
			sPort.close();
			throw new IOException("unsupported comm operation [" + getId() + "]" + (e.getMessage() != null ? e.getMessage() : ""));
		}
		try
		{
			in = new DataInputStream(sPort.getInputStream());
			out = new DataOutputStream(sPort.getOutputStream());
		}
		catch (IOException e)
		{
			sPort.close();
			connected = false;
			throw e;
		}

		try
		{
			sPort.addEventListener(eventListener);
		}
		catch (TooManyListenersException e)
		{
			sPort.close();
			throw new IOException("too many listeners added");
		}
		sPort.notifyOnDataAvailable(true);
		sPort.notifyOnBreakInterrupt(true);
		try
		{
			sPort.enableReceiveTimeout(parameters.getSoTimeout());
		}
		catch (UnsupportedCommOperationException e)
		{
		}
		portId.removePortOwnershipListener(this);
		portId.addPortOwnershipListener(this);
		connected = true;
	}

	private void setConnectionParameters() throws UnsupportedCommOperationException
	{
		int oldBaudRate = sPort.getBaudRate();
		int oldDatabits = sPort.getDataBits();
		int oldStopbits = sPort.getStopBits();
		int oldParity = sPort.getParity();
		// int oldFlowControl = sPort.getFlowControlMode();
		try
		{
			sPort.setSerialPortParams(parameters.getBaudRate(), parameters.getDatabits(), parameters.getStopbits(), parameters.getParity());
		}
		catch (UnsupportedCommOperationException e)
		{
			parameters.setBaudRate(oldBaudRate);
			parameters.setDatabits(oldDatabits);
			parameters.setStopbits(oldStopbits);
			parameters.setParity(oldParity);
			throw e;
		}

		try
		{
			sPort.setFlowControlMode(parameters.getFlowControlIn() | parameters.getFlowControlOut());
		}
		catch (UnsupportedCommOperationException e)
		{
			throw e;
		}
	}

	@Override
	public String getId()
	{
		return parameters.toSerialString();
	}

	@Override
	public boolean connected()
	{
		return connected;
	}

	private final SerialPortEventListener eventListener = new SerialPortEventListener()
	{
		/**
		 * 接收数据
		 */
		@Override
		public synchronized void serialEvent(SerialPortEvent e)
		{
			switch (e.getEventType())
			{
				case SerialPortEvent.DATA_AVAILABLE:
					receive();
					break;
				case SerialPortEvent.BI:/* Break interrupt,通讯中断 */
				case SerialPortEvent.OE:/* Overrun error,溢位错误 */
				case SerialPortEvent.FE:/* Framing error,传帧错误 */
				case SerialPortEvent.PE:/* Parity error,校验错误 */
				case SerialPortEvent.CD:/* Carrier detect,载波检测 */
				case SerialPortEvent.CTS:/* Clear to send,清除发送 */
				case SerialPortEvent.DSR:/* Data set ready,数据设备就绪 */
				case SerialPortEvent.RI:/* Ring indicator,响铃指示 */
				case SerialPortEvent.OUTPUT_BUFFER_EMPTY:/* 输出缓冲区清空 */
					log.debug("Receive data event type：" + e.getEventType());
					break;
			}
		}
	};

	@Override
	public void ownershipChange(int paramInt)
	{
		String osName = System.getProperty("os.name"); // 操作系统名称
		String osArch = System.getProperty("os.arch"); // 操作系统构架
		String osVersion = System.getProperty("os.version"); // 操作系统版本
		log.info(osName + " " + osArch + " " + osVersion + " ownershipChange [" + getId() + "] " + paramInt);
		if (paramInt != 1)
		{
			connected = false;
		}
	}

	public static List<String> listPort()
	{
		List<String> listPort = new ArrayList<String>();
		if (SystemType.current() == SystemType.SunOS)
		{
			Enumeration<?> en = javax.comm.CommPortIdentifier.getPortIdentifiers();
			while (en.hasMoreElements())
			{
				javax.comm.CommPortIdentifier portId = (javax.comm.CommPortIdentifier) en.nextElement();
				if (portId.getPortType() == javax.comm.CommPortIdentifier.PORT_SERIAL)
				{
					listPort.add(portId.getName());
				}
			}
		}
		else
		{
			Enumeration<?> en = gnu.io.CommPortIdentifier.getPortIdentifiers();
			while (en.hasMoreElements())
			{
				gnu.io.CommPortIdentifier portId = (gnu.io.CommPortIdentifier) en.nextElement();
				if (portId.getPortType() == gnu.io.CommPortIdentifier.PORT_SERIAL)
				{
					listPort.add(portId.getName());
				}
			}
		}
		return listPort;
	}
}
