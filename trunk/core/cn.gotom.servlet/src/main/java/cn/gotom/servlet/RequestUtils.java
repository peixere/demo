package cn.gotom.servlet;

import org.apache.struts2.ServletActionContext;

import cn.gotom.sso.util.CommonUtils;

public class RequestUtils
{

	public static void printParameters()
	{
		CommonUtils.printParameters(ServletActionContext.getRequest());
	}
}
