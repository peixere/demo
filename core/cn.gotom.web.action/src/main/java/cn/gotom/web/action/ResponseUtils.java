package cn.gotom.web.action;

import org.apache.struts2.ServletActionContext;

import cn.gotom.sso.util.CommonUtils;

class ResponseUtils
{

	public static void toJSON(Object value)
	{
		CommonUtils.toJSON(ServletActionContext.getRequest(), ServletActionContext.getResponse(), value);
	}
}
