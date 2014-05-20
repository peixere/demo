package cn.gotom.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

public class FileUtils
{
	protected static final Logger log = Logger.getLogger(FileUtils.class);

	/**
	 * 复制文件，如目标文件存在并文件长度一样没不覆盖文件，否则覆盖文件
	 * 
	 * @param from
	 * @param to
	 * @throws IOException
	 */
	public static void copy(String from, String to)
	{
		try
		{
			File fromFile = new File(from);
			save(fromFile, to);
		}
		catch (Exception ex)
		{
			log.error("copy " + from + " to " + to, ex);
		}
	}

	public static void save(File fromFile, String to)
	{
		try
		{
			if (fromFile.exists() && fromFile.isFile())
			{
				File toFile = new File(to);
				if (toFile.exists() && toFile.isFile())
				{
					if (toFile.length() == fromFile.length())
					{
						log.info(toFile.getAbsolutePath() + " 文件已存在！");
						return;
					}
					else
					{
						log.info(toFile.getAbsolutePath() + " 删除文件！");
						toFile.deleteOnExit();
					}
				}
				log.info("copy " + fromFile.getPath() + " to " + to);
				InputStream in = new BufferedInputStream(new FileInputStream(fromFile));
				OutputStream out = new BufferedOutputStream(new FileOutputStream(to));
				for (int b = in.read(); b != -1; b = in.read())
				{
					out.write(b);
				}
				in.close();
				out.close();
			}
			else
			{
				log.warn(" not exists file : " + fromFile.getPath());
			}
		}
		catch (Exception ex)
		{
			log.error("copy file to " + to, ex);
		}
	}

	public byte[] read(File file)
	{
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;
		try
		{
			baos = new ByteArrayOutputStream();
			fis = new FileInputStream(file);
			int bytesRead = 0;
			int offSet = 0;
			byte[] buffer = new byte[1024];
			while ((bytesRead = fis.read(buffer, offSet, buffer.length)) > 0)
			{
				baos.write(buffer, 0, bytesRead);
			}
			buffer = baos.toByteArray();
			return buffer;
		}
		catch (Throwable ex)
		{
			log.error("read file ", ex);
			return null;
		}
		finally
		{
			try
			{
				if (fis != null)
					fis.close();
				if (baos != null)
					baos.close();
			}
			catch (Throwable ex)
			{
				log.error("read file ", ex);
			}
			System.gc();
		}
	}

	/**
	 * 
	 * 直接覆盖文件
	 * 
	 * @param from
	 * @param to
	 * @throws IOException
	 */
	public static void cover(String from, String to)
	{
		try
		{
			File fromFile = new File(from);
			if (fromFile.exists() && fromFile.isFile())
			{
				File toFile = new File(to);
				if (toFile.exists() && toFile.isFile())
				{
					toFile.deleteOnExit();
				}
				log.warn("cover " + from + " to " + to);
				InputStream in = new BufferedInputStream(new FileInputStream(fromFile));
				OutputStream out = new BufferedOutputStream(new FileOutputStream(to));
				for (int b = in.read(); b != -1; b = in.read())
				{
					out.write(b);
				}
				in.close();
				out.close();
			}
			else
			{
				log.warn(" not exists file : " + from);
			}
		}
		catch (Exception ex)
		{
			log.error("cover " + from + " to " + to, ex);
		}
	}
}
