package cn.gotom.servlet;

import org.apache.struts2.ServletActionContext;

import cn.gotom.client.util.CommonUtils;

public class ResponseUtils
{

	public static void toJSON(Object value)
	{
		CommonUtils.toJSON(ServletActionContext.getRequest(), ServletActionContext.getResponse(), value);
	}
}
