package cn.gotom.web.util.filter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;

import org.apache.log4j.Logger;

public abstract class AbstractConfigurationFilter implements Filter
{
	protected final Logger log = Logger.getLogger(getClass());

	protected final String getInitParameter(final FilterConfig filterConfig, final String propertyName, final String defaultValue)
	{
		String value = filterConfig.getInitParameter(propertyName);

		if (value != null && value.trim().length() > 0)
		{
			return value;
		}
		value = filterConfig.getServletContext().getInitParameter(propertyName);
		return value;
	}

	protected final boolean parseBoolean(final String value)
	{
		return ((value != null) && value.equalsIgnoreCase("true"));
	}
}
