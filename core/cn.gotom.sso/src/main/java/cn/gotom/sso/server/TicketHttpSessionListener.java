package cn.gotom.sso.server;

import java.util.Enumeration;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import cn.gotom.sso.TicketMap;

public class TicketHttpSessionListener implements HttpSessionListener, HttpSessionAttributeListener
{
	protected final Logger log = Logger.getLogger(getClass());

	@Override
	public void sessionCreated(HttpSessionEvent se)
	{
		log.debug("OnLine count : " + TicketMap.instance.size());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se)
	{
		TicketMap.instance.remove(se.getSession().getId());
		log.debug("OnLine count : " + TicketMap.instance.size());
		Enumeration<String> names = se.getSession().getAttributeNames();
		while (names.hasMoreElements())
		{
			String name = names.nextElement();
			TicketMap.instance.remove(name);
		}
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent se)
	{
		if (TicketMap.instance.containsKey(se.getName()))
		{
			log.debug(se.getName() + "=" + TicketMap.instance.get(se.getName()).getUser());
		}
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent se)
	{
		if (TicketMap.instance.containsKey(se.getName()))
		{
			log.debug("remove ticket : "+se.getName());
			TicketMap.instance.remove(se.getName());
		}
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent se)
	{
		//log.debug(se.getName());
	}
}
