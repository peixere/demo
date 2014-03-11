package cn.gotom.sso.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class GsonExclusionStrategy implements ExclusionStrategy
{
	private String[] excludeFields;
	private Class<?>[] excludeClasses;

	public GsonExclusionStrategy(String[] excludeFields, Class<?>[] excludeClasses)
	{
		this.excludeFields = excludeFields;
		this.excludeClasses = excludeClasses;
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz)
	{
		if (this.excludeClasses == null)
		{
			return false;
		}
		for (Class<?> excludeClass : excludeClasses)
		{
			if (excludeClass.getName().equals(clazz.getName()))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes field)
	{
		if (this.excludeFields == null)
		{
			return false;
		}
		for (String f : this.excludeFields)
		{
			if (f.equals(field.getName()))
			{
				return true;
			}
		}
		return false;
	}

	public String[] getExcludeFields()
	{
		return excludeFields;
	}

	public void setExcludeFields(String[] excludeFields)
	{
		this.excludeFields = excludeFields;
	}

	public Class<?>[] getExcludeClasses()
	{
		return excludeClasses;
	}

	public void setExcludeClasses(Class<?>[] excludeClasses)
	{
		this.excludeClasses = excludeClasses;
	}

}
