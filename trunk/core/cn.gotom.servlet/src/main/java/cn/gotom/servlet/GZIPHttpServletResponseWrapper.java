package cn.gotom.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class GZIPHttpServletResponseWrapper extends HttpServletResponseWrapper
{
	protected HttpServletResponse response;

	private ServletOutputStream out;

	private GZIPServletOutputStream gzipsos;

	private PrintWriter writer;

	public GZIPHttpServletResponseWrapper(HttpServletResponse response) throws IOException
	{
		super(response);
		this.response = response;
		gzipsos = new GZIPServletOutputStream(response.getOutputStream());
	}

	public ServletOutputStream getOutputStream() throws IOException
	{
		if (null == out)
		{
			if (null != writer)
			{
				throw new IllegalStateException("getWriter() has already been called on this response.");
			}
			out = gzipsos;
		}
		return out;
	}

	public PrintWriter getWriter() throws IOException
	{
		if (null == writer)
		{
			if (null != out)
			{
				throw new IllegalStateException("getOutputStream() has already been called on this response.");
			}
			writer = new PrintWriter(gzipsos);
		}
		return writer;
	}

	public void flushBuffer()
	{
		try
		{
			if (writer != null)
			{
				writer.flush();
			}
			else if (out != null)
			{
				out.flush();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void reset()
	{
		super.reset();
		try
		{
			gzipsos.reset();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

	}

	public void resetBuffer()
	{
		super.resetBuffer();
		try
		{
			gzipsos.reset();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

	}

	public void close() throws IOException
	{
		gzipsos.close();
	}
}
