package cn.gotom.comm.channel;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class SerialPortJssc extends SerialPort
{

	public SerialPortJssc(String portName)
	{
		super(portName);
	}

	@Override
	public boolean writeBytes(byte[] buffer) throws SerialPortException
	{
		if (checkPortOpened(this.getPortName()))
		{
			this.closePort();
			throw new SerialPortException(getPortName(), "writeBytes()", SerialPortException.TYPE_PORT_NOT_FOUND);
		}
		return super.writeBytes(buffer);
	}

	private boolean checkPortOpened(String portName)
	{
		if (!this.isOpened())
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
