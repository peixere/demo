package cn.gotom.client.filter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;

import org.apache.log4j.Logger;

public abstract class AbstractConfigurationFilter implements Filter
{
	protected final Logger log = Logger.getLogger(getClass());

	protected final String getInitParameter(final FilterConfig filterConfig, final String propertyName, final String defaultValue)
	{
		String value = filterConfig.getInitParameter(propertyName);
		if (isEmpty(value))
		{
			value = filterConfig.getServletContext().getInitParameter(propertyName);
		}
		if (isEmpty(value))
		{
			value = defaultValue;
		}
		log.debug(propertyName);
		return value;
	}

	protected boolean isEmpty(String value)
	{
		return (value != null) ? (value.trim().length() == 0) : true;
	}

	protected final boolean parseBoolean(final String value)
	{
		return ((value != null) && value.equalsIgnoreCase("true"));
	}
}
