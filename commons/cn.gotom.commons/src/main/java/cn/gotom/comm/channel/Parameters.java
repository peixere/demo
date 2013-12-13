package cn.gotom.comm.channel;

import java.util.UUID;

public class Parameters
{
	private String address = "127.0.0.1";
	private int port = 8090;
	private String portName = "COM1";
	private int baudRate = 9600;
	private int flowControlIn;
	private int flowControlOut;
	private int databits;
	private int stopbits;
	private int parity;
	private int localPort;
	private String BluePort = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB").toString();
	private int soTimeout = 100;
	private int hidVID;
	private int hidPID;

	private ChannelTypeEnum channelType = ChannelTypeEnum.TCP;

	public Parameters()
	{
		channelType = ChannelTypeEnum.TCP;
	}

	public Parameters(UUID bluePort)
	{
		channelType = ChannelTypeEnum.Bluetooth;
		BluePort = bluePort.toString();
	}

	public Parameters(String host, int port)
	{
		channelType = ChannelTypeEnum.TCP;
		this.address = host;
		this.port = port;
	}

	public Parameters(String host, int port, int localPort)
	{
		this(host, port);
		this.localPort = localPort;
		channelType = ChannelTypeEnum.UDP;
	}

	public Parameters(int hidVID, int hidPID)
	{
		this.hidVID = hidVID;
		this.hidPID = hidPID;
		channelType = ChannelTypeEnum.HID;
	}

	public Parameters(String portName, int baudRate, int databits, int stopbits, int parity)
	{
		this(portName, baudRate, 0, 0, baudRate, databits, parity);
	}

	public Parameters(String portName, int baudRate, int flowControlIn, int flowControlOut, int databits, int stopbits, int parity)
	{
		this.setPortName(portName);
		this.setBaudRate(baudRate);
		this.setFlowControlIn(flowControlIn);
		this.setFlowControlOut(flowControlOut);
		this.setDatabits(databits);
		this.setParity(parity);
		this.setStopbits(stopbits);
		channelType = ChannelTypeEnum.SerialPort;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public String getPortName()
	{
		return portName;
	}

	public void setPortName(String portName)
	{
		this.portName = portName;
		this.channelType = ChannelTypeEnum.SerialPort;
	}

	public int getBaudRate()
	{
		return baudRate;
	}

	public void setBaudRate(int baudRate)
	{
		this.baudRate = baudRate;
	}

	public int getFlowControlIn()
	{
		return flowControlIn;
	}

	public void setFlowControlIn(int flowControlIn)
	{
		this.flowControlIn = flowControlIn;
	}

	public int getFlowControlOut()
	{
		return flowControlOut;
	}

	public void setFlowControlOut(int flowControlOut)
	{
		this.flowControlOut = flowControlOut;
	}

	public int getDatabits()
	{
		return databits;
	}

	public void setDatabits(int databits)
	{
		this.databits = databits;
	}

	public int getStopbits()
	{
		return stopbits;
	}

	public void setStopbits(int stopbits)
	{
		this.stopbits = stopbits;
	}

	public int getParity()
	{
		return parity;
	}

	public void setParity(int parity)
	{
		this.parity = parity;
	}

	public int getLocalPort()
	{
		return localPort;
	}

	public void setLocalPort(int localPort)
	{
		this.localPort = localPort;
	}

	public String getBluePort()
	{
		return BluePort;
	}

	public void setBluePort(String bluePort)
	{
		BluePort = bluePort;
	}

	public int getSoTimeout()
	{
		return soTimeout;
	}

	public void setSoTimeout(int soTimeout)
	{
		this.soTimeout = soTimeout;
	}

	public int getHidVID()
	{
		return hidVID;
	}

	public void setHidVID(int hidVID)
	{
		this.hidVID = hidVID;
	}

	public int getHidPID()
	{
		return hidPID;
	}

	public void setHidPID(int hidPID)
	{
		this.hidPID = hidPID;
	}

	public ChannelTypeEnum getChannelType()
	{
		return channelType;
	}

	public void setChannelType(ChannelTypeEnum channelType)
	{
		this.channelType = channelType;
	}

	@Override
	public String toString()
	{
		switch (channelType)
		{
			case SerialPort:
				return toSerialString();
			case TCP:
				return toTcpString();
			case UDP:
				return toUdpString();
			case Bluetooth:
				return toBlueString();
			case HID:
				return this.toHidString();
		}
		return super.toString();
	}

	public String toBlueString()
	{
		return "Bluetooth{" + BluePort + "}";
	}

	public String toTcpString()
	{
		return "tcp{" + address + ":" + port + "}";
	}

	public String toHidString()
	{
		return "hid{vid=" + hidVID + ";pid=" + hidPID + "}";
	}

	public String toSerialString()
	{
		return "SerialPort{" + this.portName + ":" + this.baudRate + "}";
	}

	public String toUdpString()
	{
		return "udp{" + address + ":" + port + ";local:" + localPort + "}";
	}
}
