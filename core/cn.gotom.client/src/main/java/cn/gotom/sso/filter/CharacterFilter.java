package cn.gotom.sso.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import cn.gotom.sso.util.CommonUtils;

public class CharacterFilter extends AbstractConfigurationFilter
{
	private String encoding;

	private boolean forceEncoding = false;

	public void setEncoding(String encoding)
	{
		this.encoding = encoding;
	}

	public void setForceEncoding(boolean forceEncoding)
	{
		this.forceEncoding = forceEncoding;
	}

	@Override
	public void destroy()
	{
		log.info("destroy");

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
	{
		if (this.encoding != null && (this.forceEncoding || request.getCharacterEncoding() == null))
		{
			request.setCharacterEncoding(this.encoding);
			if (this.forceEncoding)
			{
				response.setCharacterEncoding(this.encoding);
			}
		}
		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		setEncoding(getInitParameter(filterConfig, "encoding", null));
		setForceEncoding(CommonUtils.parseBoolean(getInitParameter(filterConfig, "forceEncoding", null)));
	}
}
