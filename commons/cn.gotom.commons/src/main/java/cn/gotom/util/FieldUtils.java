package cn.gotom.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class FieldUtils
{

	// ~ Methods ========================================================================================================
	/**
	 * Attempts to locate the specified field on the class.
	 * 
	 * @param clazz
	 *            the class definition containing the field
	 * @param fieldName
	 *            the name of the field to locate
	 * 
	 * @return the Field (never null)
	 * 
	 * @throws IllegalStateException
	 *             if field could not be found
	 */
	public static Field getField(Class<?> clazz, String fieldName) throws IllegalStateException
	{
		Assert.notNull(clazz, "Class required");
		Assert.hasText(fieldName, "Field name required");

		try
		{
			return clazz.getDeclaredField(fieldName);
		}
		catch (NoSuchFieldException nsf)
		{
			// Try superclass
			if (clazz.getSuperclass() != null)
			{
				return getField(clazz.getSuperclass(), fieldName);
			}

			throw new IllegalStateException("Could not locate field '" + fieldName + "' on class " + clazz);
		}
	}

	/**
	 * Returns the value of a (nested) field on a bean. Intended for testing.
	 * 
	 * @param bean
	 *            the object
	 * @param fieldName
	 *            the field name, with "." separating nested properties
	 * @return the value of the nested field
	 */
	public static Object getFieldValue(Object bean, String fieldName) throws IllegalAccessException
	{
		Assert.notNull(bean, "Bean cannot be null");
		Assert.hasText(fieldName, "Field name required");
		String[] nestedFields = StringUtils.tokenizeToStringArray(fieldName, ".");
		Class<?> componentClass = bean.getClass();
		Object value = bean;

		for (String nestedField : nestedFields)
		{
			Field field = getField(componentClass, nestedField);
			field.setAccessible(true);
			value = field.get(value);
			if (value != null)
			{
				componentClass = value.getClass();
			}
		}

		return value;

	}

	public static Object getProtectedFieldValue(String protectedField, Object object)
	{
		Field field = FieldUtils.getField(object.getClass(), protectedField);

		try
		{
			field.setAccessible(true);

			return field.get(object);
		}
		catch (Exception ex)
		{
			ReflectionUtils.handleReflectionException(ex);

			return null; // unreachable - previous line throws exception
		}
	}

	public static void setProtectedFieldValue(String protectedField, Object object, Object newValue)
	{
		Field field = FieldUtils.getField(object.getClass(), protectedField);

		try
		{
			field.setAccessible(true);
			field.set(object, newValue);
		}
		catch (Exception ex)
		{
			ReflectionUtils.handleReflectionException(ex);
		}
	}

	public static String getFields(Class<?> clazz)
	{
		Field[] fields = clazz.getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		for (Field f : fields)
		{
			if (!Modifier.isStatic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers()))
				sb.append("'" + f.getName() + "', ");
		}
		while ((clazz = clazz.getSuperclass()) != null)
		{
			fields = clazz.getDeclaredFields();
			for (Field f : fields)
			{
				if (!Modifier.isStatic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers()))
					sb.append("'" + f.getName() + "', ");
			}
		}
		if (sb.length() > 2)
		{
			return (sb.substring(0, sb.length() - 2));
		}
		else
		{
			return sb.toString();
		}
	}
}
