package cn.gotom.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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

	public static void cover(String from, String to) throws IOException
	{
		try
		{
			File fromFile = new File(from);
			File toFile = new File(to);
			if (fromFile.exists() && fromFile.isFile())
			{
				if (toFile.exists() && toFile.isFile())
				{
					if (toFile.length() == fromFile.length())
					{
						log.warn(toFile.getAbsolutePath() + " file exists");
						return;
					}
					else
					{
						log.warn(toFile.getAbsolutePath() + " deleteOnExit");
						toFile.deleteOnExit();
					}
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
		}
		catch (Exception ex)
		{
			log.error("copy " + from + " to " + to, ex);
		}
	}
}
