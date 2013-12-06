package cn.gotom.servlet;

import org.apache.struts2.ServletActionContext;

import cn.gotom.sso.util.CommonUtils;

public class ResponseUtils
{

	public static void toJSON(Object value)
	{
		CommonUtils.toJSON(ServletActionContext.getRequest(), ServletActionContext.getResponse(), value);
	}
}
