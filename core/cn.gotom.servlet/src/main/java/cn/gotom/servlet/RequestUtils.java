package cn.gotom.servlet;

import org.apache.struts2.ServletActionContext;

public class RequestUtils
{

	public static void printParameters()
	{
		cn.gotom.web.util.RequestUtils.printParameters(ServletActionContext.getRequest());
	}
}
