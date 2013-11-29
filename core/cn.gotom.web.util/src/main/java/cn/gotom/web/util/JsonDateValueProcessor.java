package cn.gotom.web.util;

import java.text.SimpleDateFormat;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JsonDateValueProcessor implements JsonValueProcessor
{

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public JsonDateValueProcessor()
	{
	}

	public JsonDateValueProcessor(String dateFormat)
	{
		this.dateFormat = new SimpleDateFormat(dateFormat);
	}

	@Override
	public Object processArrayValue(Object value, JsonConfig jsonConfig)
	{
		return null;
	}

	@Override
	public Object processObjectValue(String key, Object value, JsonConfig jsonConfig)
	{
		if (value == null)
		{
			return "";
		}
		if (value instanceof java.sql.Timestamp || value instanceof java.util.Date)
		{
			String str = dateFormat.format((java.util.Date) value);
			return str;
		}
		return value;
	}

}
