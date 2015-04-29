package cn.gotom.jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JMSServiceImpl implements JMSService, MessageListener, ExceptionListener
{
	private static final String url = "tcp://192.168.0.114:61616";
	private static final String QUEUE_NAME = "choice.queue";

	private Connection connection;
	private Session session;
	private MessageConsumer consumer;

	public void start()
	{
		try
		{
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
			connection = connectionFactory.createConnection();
			connection.start();
			connection.setExceptionListener(this);
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(QUEUE_NAME);
			consumer = session.createConsumer(destination);
			//session.setMessageListener(this);
			consumer.setMessageListener(this);
		}
		catch (JMSException e)
		{
			System.out.println("接收服务启动失败 " + e.toString());
			e.printStackTrace();
		}
	}

	public void onMessage(Message message)
	{
		try
		{
			if (message instanceof TextMessage)
			{
				TextMessage txtMsg = (TextMessage) message;
				String msg = txtMsg.getText();
				System.out.println("Received: " + msg);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public static void main(String args[])
	{
		JMSServiceImpl rm = new JMSServiceImpl();
		rm.start();
	}

	@Override
	public void onException(JMSException e)
	{
		destroy();
		System.out.println("异常退出 " + e.toString());
		start();
	}

	@Override
	public void init()
	{
		start();
	}

	@Override
	public void destroy()
	{
		try
		{
			if (consumer != null)
				consumer.close();
			if (session != null)
				session.close();
			if (connection != null)
				connection.close();
		}
		catch (JMSException e)
		{
			e.printStackTrace();
		}

	}

}
