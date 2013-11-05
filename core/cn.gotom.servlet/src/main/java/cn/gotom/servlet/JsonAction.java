package cn.gotom.servlet;

import java.io.IOException;
import java.util.Map;

import net.sf.json.JSON;

import org.apache.struts2.ServletActionContext;

public abstract class JsonAction
{
	public void toJSON(Object value) throws IOException
	{
		JSON json = net.sf.json.JSONSerializer.toJSON(value);
		String encoing = ServletActionContext.getRequest().getCharacterEncoding();
		ServletActionContext.getResponse().setContentType("text/html;charset=" + encoing);
		ServletActionContext.getResponse().getWriter().println(json.toString());
		ServletActionContext.getResponse().getWriter().flush();
		ServletActionContext.getResponse().getWriter().close();
	}

	public static void writerToJSON(Object value) throws IOException
	{
		JSON json = net.sf.json.JSONSerializer.toJSON(value);
		String encoing = ServletActionContext.getRequest().getCharacterEncoding();
		ServletActionContext.getResponse().setContentType("text/html;charset=" + encoing);
		ServletActionContext.getResponse().getWriter().println(json.toString());
		ServletActionContext.getResponse().getWriter().flush();
		ServletActionContext.getResponse().getWriter().close();
	}

	public static void printParameters() throws IOException
	{
		Map<String, String[]> params = ServletActionContext.getRequest().getParameterMap();
		for (String key : params.keySet())
		{
			String[] values = params.get(key);
			for (String value : values)
			{
				System.out.println(key + "=" + value);
			}
		}
	}
}
