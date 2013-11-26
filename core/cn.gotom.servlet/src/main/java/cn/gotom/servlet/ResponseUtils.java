package cn.gotom.servlet;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import net.sf.json.JSON;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class ResponseUtils
{
	private static final Logger log = Logger.getLogger(ResponseUtils.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	private static final JsonValueProcessor jsonDataValueProcessor = new JsonValueProcessor()
	{

		@Override
		public Object processArrayValue(Object arg0, JsonConfig arg1)
		{
			return null;
		}

		@Override
		public Object processObjectValue(String key, Object value, JsonConfig jsonConfig)
		{
			if (value == null)
			{
				return "";
			}
			if (value instanceof java.sql.Timestamp || value instanceof java.util.Date)
			{
				String str = dateFormat.format((java.util.Date) value);
				return str;
			}
			return value;
		}

	};

	public static void toJSON(Object value)
	{
		JsonConfig config = new JsonConfig();
		config.setIgnoreDefaultExcludes(false);
		config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		config.registerJsonValueProcessor(java.sql.Timestamp.class, jsonDataValueProcessor);
		config.registerJsonValueProcessor(java.util.Date.class, jsonDataValueProcessor);
		JSON json = net.sf.json.JSONSerializer.toJSON(value, config);
		String encoing = ServletActionContext.getRequest().getCharacterEncoding();
		// ServletActionContext.getResponse().setContentType("text/html;charset=" + encoing);
		ServletActionContext.getResponse().setContentType("application/json;charset=" + encoing);
		try
		{
			ServletActionContext.getResponse().getWriter().println(json.toString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			log.error("输出JSON异常 " + value);
		}
		finally
		{
			try
			{
				ServletActionContext.getResponse().getWriter().flush();
				ServletActionContext.getResponse().getWriter().close();
			}
			catch (IOException e)
			{
				log.error("输出JSON异常 " + value);
			}
		}
	}

	public static String formatXML(String inputXML)
	{
		SAXReader reader = new SAXReader();
		XMLWriter writer = null;
		try
		{
			Document document = (Document) reader.read(new StringReader(inputXML));
			StringWriter stringWriter = new StringWriter();
			OutputFormat format = new OutputFormat(" ", true);
			writer = new XMLWriter(stringWriter, format);
			writer.write(document);
			writer.flush();
			inputXML = stringWriter.getBuffer().toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage());
		}
		finally
		{
			if (writer != null)
			{
				try
				{
					writer.close();
				}
				catch (IOException e)
				{
				}
			}
		}
		return inputXML;
	}
}
