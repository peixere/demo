package cn.gotom.sso.server;

import java.util.Enumeration;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import cn.gotom.sso.TicketMap;

public class TicketHttpSessionListener implements HttpSessionListener
{
	protected final Logger log = Logger.getLogger(getClass());

	@Override
	public void sessionCreated(HttpSessionEvent se)
	{
		log.debug(se.getSession().getId());
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
		log.debug(se.getSession().getId());
		TicketMap.instance.remove(se.getSession().getId());
		Enumeration<String> names = se.getSession().getAttributeNames();
		while(names.hasMoreElements())
		{
			String name = names.nextElement();
			TicketMap.instance.remove(name);
			log.info(name + "=" + se.getSession().getAttribute(name));
		}
	}

}
