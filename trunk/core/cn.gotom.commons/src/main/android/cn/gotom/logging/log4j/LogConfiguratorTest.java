package cn.gotom.logging.log4j;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import android.os.Environment;

public class LogConfiguratorTest
{

	public static void configure()
	{
		final LogConfigurator logConfigurator = new LogConfigurator();
		logConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator + "app.log");
		// Set the root log level
		logConfigurator.setRootLevel(Level.DEBUG);
		// Set log level of a specific logger
		logConfigurator.setLevel("org.apache", Level.ERROR);
		logConfigurator.setLevel("cn.gotom", Level.ALL);
		// logConfigurator.setUseLogCatAppender(false);
		// logConfigurator.setImmediateFlush(true);
		logConfigurator.configure();
	}

	public static void main(String[] a)
	{
		final Logger log = Logger.getLogger(LogConfiguratorTest.class);
		log.info("test");
	}
}
