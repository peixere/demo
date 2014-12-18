package cn.gotom.sso.filter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import cn.gotom.sso.util.CommonUtils;

public abstract class AbstractConfigurationFilter implements Filter
{
	protected final Logger log = Logger.getLogger(getClass());
	protected String encodingAlgorithm;
	protected FilterConfig filterConfig;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		this.filterConfig = filterConfig;
		encodingAlgorithm = this.getInitParameter(filterConfig, "encodingAlgorithm", "MD5");
	}

	protected final String getInitParameter(final FilterConfig filterConfig, final String propertyName, final String defaultValue)
	{
		String value = filterConfig.getInitParameter(propertyName);
		if (CommonUtils.isNotBlank(value))
		{
			log.info("Property [" + propertyName + "] loaded from FilterConfig.getInitParameter with value [" + value + "]");
			return value;
		}
		return CommonUtils.getInitParameter(filterConfig.getServletContext(), propertyName, defaultValue);
	}
}
