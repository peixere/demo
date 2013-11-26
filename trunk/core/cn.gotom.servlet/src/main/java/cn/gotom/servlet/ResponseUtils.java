package cn.gotom.servlet;

import org.apache.struts2.ServletActionContext;

public class ResponseUtils
{

	public static void toJSON(Object value)
	{
		cn.gotom.web.util.ResponseUtils.toJSON(ServletActionContext.getRequest(), ServletActionContext.getResponse(), value);
	}
}
