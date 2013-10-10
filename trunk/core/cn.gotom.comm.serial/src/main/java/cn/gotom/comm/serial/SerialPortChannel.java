package cn.gotom.comm.serial;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.TooManyListenersException;

import javax.comm.CommPortIdentifier;
import javax.comm.CommPortOwnershipListener;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

import cn.gotom.annotation.Description;
import cn.gotom.comm.channel.ChannelType;
import cn.gotom.comm.channel.ChannelTypeEnum;
import cn.gotom.comm.channel.Parameters;
import cn.gotom.comm.channel.State;
import cn.gotom.util.Converter;
import cn.gotom.util.SystemType;

@Description("串口")
@ChannelType(ChannelTypeEnum.SerialPort)
public class SerialPortChannel extends GnuSerialChannel implements CommPortOwnershipListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CommPortIdentifier portId;
	private SerialPort sPort;

	public SerialPortChannel()
	{
		super();
	}

	public SerialPortChannel(Parameters parameters)
	{
		super();
		this.parameters = parameters;
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
			log.info("closed[" + getId() + "]");
			portId.removePortOwnershipListener(this);
		}
		sPort = null;
	}

	@Override
	public synchronized void connect() throws IOException
	{
		if (SystemType.current() == SystemType.SunOS)
		{
			if (connected)
				return;
			try
			{
				this.onState(State.Connecting);
				connectComm();
				this.onState(State.Connected);
			}
			catch (IOException ex)
			{
				this.close();
				this.onState(State.Fail);
				log.error(Thread.currentThread().getName() + "通道[" + getId() + "]连接异常：" + ex.getMessage(), ex);
				throw ex;
			}
		}
		else
		{
			super.connect();
		}
	}

	private synchronized void connectComm() throws IOException
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
			throw new IOException("找不到相应串口" + getId() + "]" + e.getMessage());
		}
		catch (PortInUseException e)
		{
			throw new IOException("串口已被占用" + getId() + "]" + e.getMessage());
		}
		catch (UnsupportedCommOperationException e)
		{
			sPort.close();
			throw new IOException("设备连接参数异常" + getId() + "]" + (e.getMessage() != null ? e.getMessage() : ""));
		}
		finally
		{

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
				case SerialPortEvent.OE:/* Overrun error，溢位错误 */
				case SerialPortEvent.FE:/* Framing error，传帧错误 */
				case SerialPortEvent.PE:/* Parity error，校验错误 */
				case SerialPortEvent.CD:/* Carrier detect，载波检测 */
				case SerialPortEvent.CTS:/* Clear to send，清除发送 */
				case SerialPortEvent.DSR:/* Data set ready，数据设备就绪 */
				case SerialPortEvent.RI:/* Ring indicator，响铃指示 */
				case SerialPortEvent.OUTPUT_BUFFER_EMPTY:/* 输出缓冲区清空 */
					log.debug("接收数据事件：" + e.getEventType());
					break;
			}
		}
	};

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
}
