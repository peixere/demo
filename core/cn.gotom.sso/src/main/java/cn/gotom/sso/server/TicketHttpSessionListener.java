package cn.gotom.sso.server;

import java.util.Enumeration;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

public class TicketHttpSessionListener implements HttpSessionListener
{
	protected final Logger log = Logger.getLogger(getClass());

	@Override
	public void sessionCreated(HttpSessionEvent se)
	{
		Enumeration<String> names = se.getSession().getAttributeNames();
		while(names.hasMoreElements())
		{
			String name = names.nextElement();
			log.info(name + "=" + se.getSession().getAttribute(name));
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se)
	{
		Enumeration<String> names = se.getSession().getAttributeNames();
		while(names.hasMoreElements())
		{
			String name = names.nextElement();
			log.info(name + "=" + se.getSession().getAttribute(name));
		}
	}

}
