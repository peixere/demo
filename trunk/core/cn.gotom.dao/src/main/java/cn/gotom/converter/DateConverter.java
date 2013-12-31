package cn.gotom.converter;

import java.util.Date;

import org.apache.commons.beanutils.converters.DateTimeConverter;

public class DateConverter extends DateTimeConverter
{
	@Override
	public Object convert(@SuppressWarnings("rawtypes") Class type, Object value)
	{
		if (value == null)
		{
			return value;
		}
		return super.convert(type, value);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getDefaultType()
	{
		return Date.class;
	}

}
