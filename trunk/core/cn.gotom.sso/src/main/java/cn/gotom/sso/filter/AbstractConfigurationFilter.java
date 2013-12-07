package cn.gotom.sso.filter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;

import org.apache.log4j.Logger;

import cn.gotom.sso.util.CommonUtils;

public abstract class AbstractConfigurationFilter implements Filter
{
	protected final Logger log = Logger.getLogger(getClass());

	protected final String getInitParameter(final FilterConfig filterConfig, final String propertyName, final String defaultValue)
	{
		String value = filterConfig.getInitParameter(propertyName);
		if (CommonUtils.isNotBlank(value))
		{
			log.info("Property [" + propertyName + "] loaded from FilterConfig.getInitParameter with value [" + value + "]");
			return value;
		}
		value = filterConfig.getServletContext().getInitParameter(propertyName);
		if (CommonUtils.isNotBlank(value))
		{
			log.info("Property [" + propertyName + "] loaded from ServletContext.getInitParameter with value [" + value + "]");
			return value;
		}
		if (CommonUtils.isEmpty(value))
		{
			value = defaultValue;
		}
		log.info("Property [" + propertyName + "] not found.  Using default value [" + defaultValue + "]");
		return defaultValue;
	}
}
