package cn.gotom.client.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class ResponseUtils
{
	private static final Logger log = Logger.getLogger(ResponseUtils.class);

	public static void toJSON(HttpServletRequest request, HttpServletResponse response, Object value, String dateFormat)
	{
		JsonConfig config = new JsonConfig();
		config.setIgnoreDefaultExcludes(false);
		config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		if (dateFormat != null && dateFormat.trim().length() > 0)
		{
			JsonValueProcessor dateValueProcessor = new JsonDateValueProcessor();
			config.registerJsonValueProcessor(java.sql.Timestamp.class, dateValueProcessor);
			config.registerJsonValueProcessor(java.util.Date.class, dateValueProcessor);
		}
		JSON json = net.sf.json.JSONSerializer.toJSON(value, config);
		String encoing = request.getCharacterEncoding();
		// response.setContentType("text/html;charset=" + encoing);
		response.setContentType("application/json;charset=" + encoing);
		try
		{
			response.getWriter().println(json.toString());
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
				response.getWriter().flush();
				response.getWriter().close();
			}
			catch (IOException e)
			{
				log.error("输出JSON异常 " + value);
			}
		}
	}

	public static void toJSON(HttpServletRequest request, HttpServletResponse response, Object value)
	{
		toJSON(request, response, value, null);
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
