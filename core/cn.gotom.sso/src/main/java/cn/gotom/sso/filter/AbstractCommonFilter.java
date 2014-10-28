package cn.gotom.sso.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import cn.gotom.sso.util.CommonUtils;
import cn.gotom.util.PasswordEncoder;
import cn.gotom.util.PasswordEncoderMessageDigest;

public abstract class AbstractCommonFilter extends AbstractConfigurationFilter
{
	protected final Logger log = Logger.getLogger(getClass());
	public static final String serverLoginUrlParameter = "serverLoginUrl";
	public static final String THIS = "${this}";

	private String ticketParameterName = "ticket";

	private String serviceParameterName = "service";

	private boolean encodeServiceUrl = true;
	/**
	 * 必选，验证服务器URL
	 */
	protected String serverLoginUrl;
	private String encodingAlgorithm;
	protected PasswordEncoder passwordEncoder;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		initInternal(filterConfig);
		passwordEncoder = new PasswordEncoderMessageDigest(encodingAlgorithm);
	}

	protected void initInternal(final FilterConfig filterConfig) throws ServletException
	{
		encodingAlgorithm = this.getInitParameter(filterConfig, "encodingAlgorithm", "MD5");
		setTicketParameterName(getInitParameter(filterConfig, "ticketParameterName", "ticket"));
		setServiceParameterName(getInitParameter(filterConfig, "serviceParameterName", "service"));
		setEncodeServiceUrl(CommonUtils.parseBoolean(getInitParameter(filterConfig, "encodeServiceUrl", "true")));
		setServerLoginUrl(getInitParameter(filterConfig, serverLoginUrlParameter, null));
	}

	public boolean getEncodeServiceUrl()
	{
		return encodeServiceUrl;
	}

	public void setEncodeServiceUrl(boolean encodeServiceUrl)
	{
		this.encodeServiceUrl = encodeServiceUrl;
	}

	public String getTicketParameterName()
	{
		return ticketParameterName;
	}

	public void setTicketParameterName(String ticketParameterName)
	{
		this.ticketParameterName = ticketParameterName;
	}

	public String getServiceParameterName()
	{
		return serviceParameterName;
	}

	public void setServiceParameterName(String serviceParameterName)
	{
		this.serviceParameterName = serviceParameterName;
	}

	public String getServerLoginUrl()
	{
		return serverLoginUrl;
	}

	public void setServerLoginUrl(String serverLoginUrl)
	{
		this.serverLoginUrl = serverLoginUrl;
	}

}
