package cn.gotom.comm.test;

import java.awt.Choice;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.gotom.comm.channel.Parameters;
import cn.gotom.comm.channel.ParametersConfig;
import cn.gotom.comm.channel.SerialParameters;
import cn.gotom.comm.channel.SerialPortChannel;
import cn.gotom.util.StringUtils;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SerialConfigPanel extends JPanel implements ItemListener, ParametersConfig
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel portNameLabel;
	private Choice portChoice;
	private JLabel baudLabel;
	private Choice baudChoice;
	// private JLabel flowControlInLabel;
	private Choice flowChoiceIn;
	// private JLabel flowControlOutLabel;
	private Choice flowChoiceOut;
	private JLabel databitsLabel;
	private Choice databitsChoice;
	private JLabel stopbitsLabel;
	private Choice stopbitsChoice;
	private JLabel parityLabel;
	private Choice parityChoice;

	private SerialParameters parameters;

	public SerialConfigPanel()
	{
		parameters = new SerialParameters();
		GridLayout gridLayout = new GridLayout(3, 4);
		gridLayout.setVgap(5);
		setLayout(gridLayout);
		portNameLabel = new JLabel("portName", Label.LEFT);
		add(portNameLabel);
		portChoice = new Choice();
		portChoice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				listPortChoices();
			}
		});
		portChoice.addItemListener(this);
		portChoice.add("");
		add(portChoice);

		baudLabel = new JLabel("Baud Rate", Label.LEFT);
		add(baudLabel);
		baudChoice = new Choice();
		baudChoice.addItem("300");
		baudChoice.addItem("2400");
		baudChoice.addItem("9600");
		baudChoice.addItem("14400");
		baudChoice.addItem("28800");
		baudChoice.addItem("38400");
		baudChoice.addItem("57600");
		baudChoice.addItem("115200");
		baudChoice.addItemListener(this);
		add(baudChoice);
		// flowControlInLabel = new JLabel("Flow Control In:", Label.LEFT);
		// add(flowControlInLabel);
		flowChoiceIn = new Choice();
		flowChoiceIn.addItem("None");
		flowChoiceIn.addItem("Xon/Xoff In");
		flowChoiceIn.addItem("RTS/CTS In");
		flowChoiceIn.addItemListener(this);
		// add(flowChoiceIn);
		// flowControlOutLabel = new JLabel("Flow Control Out:", Label.LEFT);
		// add(flowControlOutLabel);
		flowChoiceOut = new Choice();
		flowChoiceOut.addItem("None");
		flowChoiceOut.addItem("Xon/Xoff Out");
		flowChoiceOut.addItem("RTS/CTS Out");
		flowChoiceOut.addItemListener(this);
		// add(flowChoiceOut);
		databitsLabel = new JLabel("Data Bits", Label.LEFT);
		add(databitsLabel);
		databitsChoice = new Choice();
		databitsChoice.addItem("5");
		databitsChoice.addItem("6");
		databitsChoice.addItem("7");
		databitsChoice.addItem("8");
		databitsChoice.addItemListener(this);
		add(databitsChoice);
		stopbitsLabel = new JLabel("Stop Bits", Label.LEFT);
		add(stopbitsLabel);
		stopbitsChoice = new Choice();
		stopbitsChoice.addItem("1");
		stopbitsChoice.addItem("1.5");
		stopbitsChoice.addItem("2");
		stopbitsChoice.addItemListener(this);
		add(stopbitsChoice);
		parityLabel = new JLabel("Parity", Label.LEFT);
		add(parityLabel);
		parityChoice = new Choice();
		parityChoice.addItem("None");
		parityChoice.addItem("Even");
		parityChoice.addItem("Odd");
		parityChoice.select("None");
		parityChoice.addItemListener(this);
		add(parityChoice);
		listPortChoices();
		setConfig();
		this.setBounds(0, 0, 300, 80);
	}

	private void setConfig()
	{
		if (StringUtils.isNullOrEmpty(parameters.getPortName()))
		{
			if (portChoice.getItemCount() > 0)
			{
				parameters.setPortName(portChoice.getItem(0));
				portChoice.select(0);
			}
		}
		else
		{
			boolean has = false;
			for (int i = 0; i < portChoice.getItemCount(); i++)
			{
				String item = portChoice.getItem(i);
				if (item.equals(parameters.getPortName()))
				{
					has = true;
					break;
				}
			}
			if (!has)
			{
				portChoice.add(parameters.getPortName());
			}
			portChoice.select(parameters.getPortName());
		}
		baudChoice.select(parameters.getBaudRateString());
		flowChoiceIn.select(parameters.getFlowControlInString());
		flowChoiceOut.select(parameters.getFlowControlOutString());
		databitsChoice.select(parameters.getDatabitsString());
		stopbitsChoice.select(parameters.getStopbitsString());
		parityChoice.select(parameters.getParityString());
	}

	private void setParameters()
	{
		parameters.setPortName(portChoice.getSelectedItem());
		parameters.setBaudRate(baudChoice.getSelectedItem());
		parameters.setFlowControlIn(flowChoiceIn.getSelectedItem());
		parameters.setFlowControlOut(flowChoiceOut.getSelectedItem());
		parameters.setDatabits(databitsChoice.getSelectedItem());
		parameters.setStopbits(stopbitsChoice.getSelectedItem());
		parameters.setParity(parityChoice.getSelectedItem());
	}

	private void listPortChoices()
	{
		try
		{
			portChoice.removeAll();
			for (String name : SerialPortChannel.listPort())
			{
				portChoice.addItem(name);
			}
			if (StringUtils.isNullOrEmpty(parameters.getPortName()))
			{
				if (portChoice.getItemCount() > 0)
				{
					parameters.setPortName(portChoice.getItem(0));
					portChoice.select(0);
				}
			}
			else
			{
				portChoice.select(parameters.getPortName());
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void setParameters(SerialParameters parameters)
	{
		this.parameters = parameters;
		this.setConfig();
	}

	@Override
	public Parameters getParameters()
	{
		Parameters param = new Parameters();
		param.setPortName(parameters.getPortName());
		param.setBaudRate(parameters.getBaudRate());
		param.setFlowControlIn(parameters.getFlowControlIn());
		param.setFlowControlOut(parameters.getFlowControlOut());
		param.setDatabits(parameters.getDatabits());
		param.setStopbits(parameters.getStopbits());
		param.setParity(parameters.getParity());
		return param;
	}

	@Override
	public void itemStateChanged(ItemEvent e)
	{
		setParameters();
	}

	@Override
	public void setParameters(Parameters parameters)
	{
		try
		{
			this.parameters = new SerialParameters();
			this.parameters.setParity(parameters.getPortName());
			this.parameters.setAddress(parameters.getAddress());
			this.parameters.setBaudRate(parameters.getBaudRate());
			this.parameters.setDatabits(parameters.getDatabits());
			this.parameters.setFlowControlIn(parameters.getFlowControlIn());
			this.parameters.setFlowControlOut(parameters.getFlowControlOut());
			this.parameters.setParity(parameters.getParity());
			this.parameters.setPort(parameters.getPort());
			this.parameters.setPortName(parameters.getPortName());
			this.parameters.setStopbits(parameters.getStopbits());
		}
		catch (Exception ex)
		{

		}
		this.setConfig();
	}

}