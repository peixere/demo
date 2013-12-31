package cn.gotom.converter;

import java.util.Date;

import org.apache.commons.beanutils.converters.DateTimeConverter;

public class DateConverter extends DateTimeConverter
{
	public DateConverter()
	{
		setDefaultValue(new Date());
	}

	@Override
	public Object convert(@SuppressWarnings("rawtypes") Class type, Object value)
	{
		if (value == null)
		{
			value = new Date();
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
