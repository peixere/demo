package cn.gotom.comm.test;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

import cn.gotom.comm.channel.Channel;
import cn.gotom.comm.channel.SerialPortChannel;
import cn.gotom.commons.Listener;
import cn.gotom.util.Converter;

class SerialDemo extends JFrame
{
	public static void main(String[] agrs)
	{
		byte b = (byte) 127;
		int i = b;
		if (i < 0)
			i = 256 + i;
		try
		{
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
					JFrame frame = new SerialDemo();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public SerialDemo()
	{
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				if (client != null && client.connected())
				{
					client.close();
				}
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		this.setMinimumSize(new Dimension(360, 430));
		config = new SerialConfigPanel();

		btnConn = new JButton("Open");
		btnConn.setActionCommand("conn");
		btnConn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SerialDemo.this.actionPerformed(e);
			}
		});

		btnSend = new JButton("Send");
		btnConn.setActionCommand("send");
		btnSend.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SerialDemo.this.actionPerformed(e);
			}
		});

		JScrollPane scrollPane = new JScrollPane();

		JScrollPane scrollPaneIn = new JScrollPane();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addGap(3).addComponent(config, GroupLayout.PREFERRED_SIZE, 348, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup().addGap(123).addComponent(btnConn).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE))
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE).addComponent(scrollPaneIn, GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
				groupLayout.createSequentialGroup().addContainerGap().addComponent(config, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE).addGap(10).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(btnConn).addComponent(btnSend)).addGap(4)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addComponent(scrollPaneIn, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE).addContainerGap()));

		textAreaIn = new JTextArea();
		textAreaIn.setEditable(false);
		scrollPaneIn.setViewportView(textAreaIn);

		textAreaOut = new JTextArea();
		textAreaOut.setText("EB 90 EB 90 02 01 00 00 01 00 03 07 00 01 12 07 23 14 09 50 A3 1A");
		scrollPane.setViewportView(textAreaOut);
		getContentPane().setLayout(groupLayout);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(btnConn))
		{
			if (client != null && client.connected())
			{
				client.close();
				btnConn.setText("Open");
			}
			else
			{
				client = new SerialPortChannel(config.getParameters());
				try
				{
					client.addReceiveListener(new Listener<byte[]>()
					{
						@Override
						public void onListener(Object sender, byte[] e)
						{
							receiveListener(sender, e);
						}
					});
					client.connect();
					btnConn.setText("Close");
				}
				catch (IOException ex)
				{
					Alert.showDialog(this, "连接异常", ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
		else if (e.getSource().equals(btnSend))
		{
			byte[] buffer = Converter.toBytes(textAreaOut.getText());
			try
			{
				client.write(buffer);
			}
			catch (IOException ex)
			{
				Alert.showDialog(this, "发送异常", ex.getMessage());
				ex.printStackTrace();
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
	private Channel client;
	private SerialConfigPanel config;
	private JTextArea textAreaIn;
	private JTextArea textAreaOut;

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

		Alert(Frame parent, String title, String message)
		{
			super(parent, title, true);
			Panel labelPanel = new Panel();
			String[] tmps = message.split("\n");
			int length = tmps.length;
			int width = 0;// Math.max();
			int height = 0;
			getContentPane().add(labelPanel, "Center");
			if (length == 1)
			{
				labelPanel.add(new Label(message, Label.CENTER));
			}
			else
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
}
