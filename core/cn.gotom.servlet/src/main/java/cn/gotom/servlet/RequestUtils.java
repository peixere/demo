package cn.gotom.servlet;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

public class RequestUtils
{
	private static final Logger log = Logger.getLogger(RequestUtils.class);

	public static void printParameters()
	{
		Map<String, String[]> params = ServletActionContext.getRequest().getParameterMap();
		StringBuffer sb = new StringBuffer();
		for (String key : params.keySet())
		{
			sb.append("\n" + key + "=");
			String[] values = params.get(key);
			for (String value : values)
			{
				sb.append(value + ",");
			}
		}
		log.debug(sb);
	}
}
