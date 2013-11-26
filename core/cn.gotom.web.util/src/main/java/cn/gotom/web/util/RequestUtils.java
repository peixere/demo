package cn.gotom.web.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class RequestUtils
{
	private static final Logger log = Logger.getLogger(RequestUtils.class);

	public static void printParameters(HttpServletRequest request)
	{
		Map<String, String[]> params = request.getParameterMap();
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
