package cn.gotom.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtils
{

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
						Logger.getLogger(FileUtils.class.getName()).warning(toFile.getAbsolutePath() + " file exists");
						return;
					}
					else
					{
						Logger.getLogger(FileUtils.class.getName()).warning(toFile.getAbsolutePath() + " deleteOnExit");
						toFile.deleteOnExit();
					}
				}
				Logger.getLogger(FileUtils.class.getName()).warning("cover " + from + " to " + to);
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
			Logger.getLogger(FileUtils.class.getName()).log(Level.WARNING, "copy " + from + " to " + to, ex);
		}
	}
}
