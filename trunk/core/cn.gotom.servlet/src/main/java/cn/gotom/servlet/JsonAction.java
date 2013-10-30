package cn.gotom.servlet;

import java.io.IOException;

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
}
