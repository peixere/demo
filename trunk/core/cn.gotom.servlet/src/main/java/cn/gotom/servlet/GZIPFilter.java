package cn.gotom.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GZIPFilter implements Filter
{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
	{
		boolean compress = false;
		if (request instanceof HttpServletRequest)
		{
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			Enumeration<String> headers = httpRequest.getHeaders("Accept-Encoding");
			while (headers.hasMoreElements())
			{
				String value = (String) headers.nextElement();
				if (value.indexOf("gzip") != -1)
					compress = true;
			}
		}
		if (compress)
		{
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.addHeader("Content-Encoding", "gzip");
			GZIPHttpServletResponseWrapper gzipResponse = new GZIPHttpServletResponseWrapper(httpResponse);
			filterChain.doFilter(request, gzipResponse);
			gzipResponse.close();
		}
		else
		{
			filterChain.doFilter(request, response);
		}
	}

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub

	}

}
