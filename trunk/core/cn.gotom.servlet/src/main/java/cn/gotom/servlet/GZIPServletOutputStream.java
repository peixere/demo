package cn.gotom.servlet;

import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;

public class GZIPServletOutputStream extends ServletOutputStream
{

	private ServletOutputStream out;

	private GZIPOutputStream gzip;

	/**
	 * 
	 * 指定压缩缓冲流
	 * 
	 * @param 输出流到压缩
	 * 
	 * @throws IOException
	 *             if an error occurs with the {@link GZIPOutputStream}.
	 */

	public GZIPServletOutputStream(ServletOutputStream out) throws IOException
	{

		this.out = out;

		reset();

	}

	/** @see ServletOutputStream * */

	public void close() throws IOException
	{

		gzip.close();

	}

	/** @see ServletOutputStream * */

	public void flush() throws IOException
	{

		gzip.flush();

	}

	/** @see ServletOutputStream * */

	public void write(byte[] b) throws IOException
	{

		write(b, 0, b.length);

	}

	/** @see ServletOutputStream * */

	public void write(byte[] b, int off, int len) throws IOException
	{

		gzip.write(b, off, len);

	}

	/** @see ServletOutputStream * */

	public void write(int b) throws IOException
	{

		gzip.write(b);

	}

	public void reset() throws IOException
	{

		gzip = new GZIPOutputStream(out);

	}

}
