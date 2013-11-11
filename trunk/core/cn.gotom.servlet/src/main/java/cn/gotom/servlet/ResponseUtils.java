package cn.gotom.servlet;

import java.io.IOException;

import net.sf.json.JSON;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.gotom.vo.JsonResponse;

public class ResponseUtils
{
	private static final Logger log = Logger.getLogger(ResponseUtils.class);

	public static void toJSON(Object value)
	{
		log.debug(value);
		JSON json = net.sf.json.JSONSerializer.toJSON(value);
		String encoing = ServletActionContext.getRequest().getCharacterEncoding();
		//ServletActionContext.getResponse().setContentType("text/html;charset=" + encoing);
		ServletActionContext.getResponse().setContentType("application/json;charset=" + encoing);
		try
		{
			ServletActionContext.getResponse().getWriter().println(json.toString());
			ServletActionContext.getResponse().getWriter().flush();
			ServletActionContext.getResponse().getWriter().close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void writer(JsonResponse value)
	{
		JSON json = net.sf.json.JSONSerializer.toJSON(value);
		String encoing = ServletActionContext.getRequest().getCharacterEncoding();
		ServletActionContext.getResponse().setContentType("text/html;charset=" + encoing);
		try
		{
			ServletActionContext.getResponse().getWriter().println(json.toString());
			ServletActionContext.getResponse().getWriter().flush();
			ServletActionContext.getResponse().getWriter().close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
