package cn.gotom.web.action;

import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;

import java.io.File;
import java.nio.charset.Charset;

public class FileCharset
{
	public static String getCharset(File file)
	{
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(JChardetFacade.getInstance());
		Charset charset = Charset.defaultCharset();
		try
		{
			charset = detector.detectCodepage(file.toURI().toURL());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (charset != null)
		{
			return charset.toString();
		}
		else
		{
			return null;
		}
	}
}
