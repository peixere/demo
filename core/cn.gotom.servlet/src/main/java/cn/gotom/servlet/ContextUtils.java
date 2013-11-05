package cn.gotom.servlet;

import java.io.IOException;
import java.util.Map;

import net.sf.json.JSON;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

public abstract class ContextUtils
{
	private static final Logger log = Logger.getLogger(ContextUtils.class);

	public static void writerToJSON(Object value) throws IOException
	{
		JSON json = net.sf.json.JSONSerializer.toJSON(value);
		String encoing = ServletActionContext.getRequest().getCharacterEncoding();
		ServletActionContext.getResponse().setContentType("text/html;charset=" + encoing);
		ServletActionContext.getResponse().getWriter().println(json.toString());
		ServletActionContext.getResponse().getWriter().flush();
		ServletActionContext.getResponse().getWriter().close();
	}

	public static void printParameters()
	{
		Map<String, String[]> params = ServletActionContext.getRequest().getParameterMap();
		for (String key : params.keySet())
		{
			String[] values = params.get(key);
			for (String value : values)
			{
				log.debug(key + "=" + value);
			}
		}
	}
}
