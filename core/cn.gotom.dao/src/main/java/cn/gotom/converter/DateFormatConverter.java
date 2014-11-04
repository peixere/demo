package cn.gotom.converter;

import java.text.SimpleDateFormat;

import org.apache.commons.beanutils.Converter;

public class DateFormatConverter implements Converter
{
	public static void main(String[] args)
	{
		
	}

	private String format;

	public DateFormatConverter(String format)
	{
		this.format = format;
	}

	@Override
	public Object convert(@SuppressWarnings("rawtypes") Class type, Object value)
	{
		String p = null;
		if (value instanceof String[])
		{
			p = ((String[]) value)[0];
		}
		else if (value instanceof String)
		{
			p = (String) value;
		}
		else if (value != null)
		{
			return value;
		}
		if (p == null || p.trim().length() == 0)
		{
			return null;
		}
		try
		{
			if (format != null || format.trim().length() > 0)
			{
				SimpleDateFormat df = new SimpleDateFormat(format);
				return df.parse(p);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public String getFormat()
	{
		return format;
	}

	public void setFormat(String format)
	{
		this.format = format;
	}

}
