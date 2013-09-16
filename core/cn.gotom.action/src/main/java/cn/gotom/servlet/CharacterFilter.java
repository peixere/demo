package cn.gotom.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.google.inject.Singleton;

@Singleton
public class CharacterFilter implements Filter
{
	protected final Logger log = Logger.getLogger(getClass());
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
		if (this.encoding != null)
		{
			if (this.forceEncoding || request.getCharacterEncoding() == null)
			{
				request.setCharacterEncoding(this.encoding);
			}
			if (this.forceEncoding || response.getCharacterEncoding() == null)
			{
				response.setCharacterEncoding(this.encoding);
			}
		}
		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		setEncoding(filterConfig.getInitParameter("encoding"));
		setForceEncoding(Boolean.parseBoolean(filterConfig.getInitParameter("forceEncoding")));
		log.info("init");
	}
}
