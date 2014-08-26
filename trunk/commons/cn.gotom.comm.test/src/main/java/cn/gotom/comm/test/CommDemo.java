package cn.gotom.comm.test;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import cn.gotom.comm.channel.Channel;
import cn.gotom.comm.channel.SerialPortChannel;
import cn.gotom.comm.channel.State;
import cn.gotom.comm.channel.TcpChannel;
import cn.gotom.comm.channel.UdpBroadcast;
import cn.gotom.comm.channel.UdpChannel;
import cn.gotom.commons.Listener;
import cn.gotom.util.Base64;
import cn.gotom.util.Converter;
import cn.gotom.util.PasswordEncoder;
import cn.gotom.util.PasswordEncoderMessageDigest;

class CommDemo extends JFrame
{
	public static void main(String[] agrs)
	{
		try
		{
			String base64 = Base64.encode("中国");
			System.out.println(base64);
			System.out.println(Base64.decode("gdyb21LQTT8229gxPtBV"));
			short s = (short) Integer.parseInt("FFE1", 16);
			System.out.println(Converter.toHexString(Converter.GetBytes(s)));
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
			{
				if ("Nimbus".equals(info.getName()))
				{
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		java.awt.EventQueue.invokeLater(new Runnable()
		{

			@Override
			public void run()
			{
				try
				{
					JFrame frame = new CommDemo();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	private int udpType = 0;

	public CommDemo()
	{
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				if (channel != null && channel.connected())
				{
					channel.close();
				}
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setResizable(false);
		this.setMinimumSize(new Dimension(600, 500));

		btnConn = new JButton("Open");
		btnConn.setActionCommand("conn");
		btnConn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				CommDemo.this.actionConn(e);
			}
		});

		btnSend = new JButton("Send");
		btnConn.setActionCommand("send");
		btnSend.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				CommDemo.this.actionSend(e);
			}
		});

		JScrollPane scrollPane = new JScrollPane();

		JScrollPane scrollPaneIn = new JScrollPane();

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 627, Short.MAX_VALUE).addComponent(scrollPaneIn, GroupLayout.DEFAULT_SIZE, 627, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE).addContainerGap())
				.addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(btnConn).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE).addContainerGap(560, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
				groupLayout.createSequentialGroup().addContainerGap().addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE).addGap(5).addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(btnSend).addComponent(btnConn))
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addComponent(scrollPaneIn, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		config = new SerialConfigPanel();
		config.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e)
			{

			}
		});
		tabbedPane.addTab("串口", null, config, null);

		panelTcp = new JPanel();
		panelTcp.setLayout(null);
		tabbedPane.addTab("TCP", null, panelTcp, null);

		addressField = new JTextField();
		addressField.setText("192.168.0.4");
		addressField.setColumns(10);
		addressField.setBounds(31, 10, 145, 32);
		panelTcp.add(addressField);

		label = new JLabel("IP:");
		label.setBounds(10, 18, 54, 15);
		panelTcp.add(label);

		label_1 = new JLabel("Port:");
		label_1.setBounds(186, 18, 54, 15);
		panelTcp.add(label_1);

		portField = new JTextField();
		portField.setText("4000");
		portField.setColumns(10);
		portField.setBounds(218, 10, 66, 32);
		panelTcp.add(portField);

		JPanel panelUdp = new JPanel();
		panelUdp.setLayout(null);
		tabbedPane.addTab("UDP", null, panelUdp, null);

		textUdpAddress = new JTextField();
		textUdpAddress.setText("192.168.0.4");
		textUdpAddress.setColumns(10);
		textUdpAddress.setBounds(31, 10, 145, 32);
		panelUdp.add(textUdpAddress);

		JLabel label_2 = new JLabel("IP:");
		label_2.setBounds(10, 18, 54, 15);
		panelUdp.add(label_2);

		JLabel label_3 = new JLabel("Port:");
		label_3.setBounds(186, 18, 54, 15);
		panelUdp.add(label_3);

		textUdpPort = new JTextField();
		textUdpPort.setText("8000");
		textUdpPort.setColumns(10);
		textUdpPort.setBounds(218, 10, 66, 32);
		panelUdp.add(textUdpPort);

		textLocalPort = new JTextField();
		textLocalPort.setText("8001");
		textLocalPort.setColumns(10);
		textLocalPort.setBounds(333, 10, 66, 32);
		panelUdp.add(textLocalPort);

		JLabel labelLocalPort = new JLabel("Local:");
		labelLocalPort.setBounds(294, 18, 54, 15);
		panelUdp.add(labelLocalPort);

		radioButtonBC = new JRadioButton("广播");
		radioButtonBC.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (radioButtonBC.isSelected())
				{
					udpType = 2;
				}
				else
				{
					udpType = 0;
				}
				radioButtonMS.setSelected(false);
			}
		});
		radioButtonBC.setBounds(418, 14, 54, 23);
		panelUdp.add(radioButtonBC);

		radioButtonMS = new JRadioButton("组播");
		radioButtonMS.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (radioButtonMS.isSelected())
				{
					udpType = 1;
				}
				else
				{
					udpType = 0;
				}
				radioButtonBC.setSelected(false);
			}
		});
		radioButtonMS.setBounds(474, 14, 54, 23);
		panelUdp.add(radioButtonMS);

		textAreaIn = new JTextArea();
		scrollPaneIn.setViewportView(textAreaIn);

		textAreaOut = new JTextArea();
		textAreaOut.setText("EB 90 EB 90 02 01 00 00 01 00 03 07 00 01 12 07 23 14 09 50 A3 1A");
		scrollPane.setViewportView(textAreaOut);
		getContentPane().setLayout(groupLayout);
	}

	private void actionSend(ActionEvent e)
	{
		byte[] buffer = Converter.toBytes(textAreaOut.getText());
		try
		{
			channel.write(buffer);
		}
		catch (Exception ex)
		{
			Alert.showDialog(this, "发送异常", ex);
		}
	}

	private void actionConn(ActionEvent e)
	{
		if (channel != null)
		{

			channel.removeAllReceiveListener();
			channel.removeAllStateListener();
			channel.close();
		}
		if (!btnConn.getText().equalsIgnoreCase("Open"))
		{
			btnConn.setText("Open");
			this.getTabbedPane().setEnabled(true);
		}
		else
		{
			switch (this.getTabbedPane().getSelectedIndex())
			{
				case 1:
					int port = Integer.parseInt(this.portField.getText());
					channel = new TcpChannel(this.addressField.getText(), port);
					break;
				case 2:
					int localPort = Integer.parseInt(this.textLocalPort.getText());
					int udpport = Integer.parseInt(this.textUdpPort.getText());
					if (udpType == 1)
					{
						// channel = new UdpMulticastClient(this.textUdpAddress.getText(), udpport, localPort);
					}
					else if (udpType == 2)
					{
						channel = new UdpBroadcast(this.textUdpAddress.getText(), udpport, localPort);
					}
					else
					{
						channel = new UdpChannel(this.textUdpAddress.getText(), udpport, localPort);
					}
					break;
				default:
					channel = new SerialPortChannel(config.getParameters());
					break;
			}

			try
			{
				channel.addReceiveListener(new Listener<byte[]>()
				{
					@Override
					public void onListener(Object sender, byte[] e)
					{
						receiveListener(sender, e);
					}
				});
				channel.addStateListener(new Listener<State>()
				{
					@Override
					public void onListener(Object sender, State state)
					{
						System.out.println(state);
						if (state == State.Connected)
						{
							btnConn.setText("Open");
						}
					}
				});
				channel.connect();
				btnConn.setText("Close");
				this.getTabbedPane().setEnabled(false);
			}
			catch (IOException ex)
			{
				Alert.showDialog(this, "连接异常", ex);
			}
		}
	}

	private void receiveListener(Object sender, byte[] buffer)
	{
		if (textAreaIn.getText() != null && textAreaIn.getText().trim().length() > 0)
		{
			textAreaIn.setText(textAreaIn.getText() + Converter.toHexString(buffer));
		}
		else
		{
			textAreaIn.setText(Converter.toHexString(buffer));
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton btnConn;
	private JButton btnSend;
	private Channel channel;
	private SerialConfigPanel config;
	private JTextArea textAreaIn;
	private JTextArea textAreaOut;
	private JPanel panelTcp;
	private JTextField addressField;
	private JLabel label;
	private JLabel label_1;
	private JTextField portField;
	private JTabbedPane tabbedPane;
	private JTextField textUdpAddress;
	private JTextField textUdpPort;
	private JTextField textLocalPort;
	private JRadioButton radioButtonBC;
	private JRadioButton radioButtonMS;

	public JTextArea getTextAreaIn()
	{
		return textAreaIn;
	}

	public JTextArea getTextAreaOut()
	{
		return textAreaOut;
	}

	static class Alert extends JDialog implements ActionListener
	{

		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;

		public static Alert showDialog(Frame parent, String title, String message)
		{
			return new Alert(parent, title, message);
		}

		public static Alert showDialog(Frame parent, String title, Throwable ex)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			return new Alert(parent, title, sw.toString());
		}

		Alert(Frame parent, String title, String message)
		{
			super(parent, title, true);
			Panel labelPanel = new Panel();
			String[] tmps = message.split("\n");
			int width = 0;// Math.max();
			int height = 150;
			getContentPane().add(labelPanel, "Center");
			{
				FontMetrics fm = getFontMetrics(getFont());
				String msg = null;
				for (String tmp : tmps)
				{
					tmp = tmp.trim();
					if (tmp.length() > 0)
					{
						int w = fm.stringWidth(tmp);
						if (w > width)
							width = w;
						height = height + fm.getHeight();
						msg = msg != null ? (msg + "\n" + tmp) : tmp;
					}
				}
				JTextArea jTextArea = new JTextArea();
				jTextArea.setEditable(false);
				jTextArea.setLineWrap(true);
				// jTextArea.setWrapStyleWord(true);
				if (height > 600)
					height = 600;
				if (width > 600)
					width = 600;
				jTextArea.setMinimumSize(new Dimension(width, height));
				jTextArea.setText(msg);
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setViewportView(jTextArea);
				GroupLayout gl_labelPanel = new GroupLayout(labelPanel);
				gl_labelPanel.setHorizontalGroup(gl_labelPanel.createParallelGroup(Alignment.LEADING).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE));
				gl_labelPanel.setVerticalGroup(gl_labelPanel.createParallelGroup(Alignment.LEADING).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE));
				labelPanel.setLayout(gl_labelPanel);
			}

			Panel buttonPanel = new Panel();
			JButton okButton = new JButton("OK");

			okButton.addActionListener(this);
			buttonPanel.add(okButton);
			getContentPane().add(buttonPanel, "South");
			setSize(width + 50, height + buttonPanel.getHeight());
			setLocation(parent.getLocationOnScreen().x + 30, parent.getLocationOnScreen().y + 30);
			setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			setVisible(false);
			dispose();
		}
	}

	protected JTabbedPane getTabbedPane()
	{
		return tabbedPane;
	}

	protected JRadioButton getRadioButton()
	{
		return radioButtonBC;
	}
}
