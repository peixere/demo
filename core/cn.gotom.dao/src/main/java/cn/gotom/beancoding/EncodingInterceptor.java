package cn.gotom.beancoding;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

import cn.gotom.util.StringUtils;

public class EncodingInterceptor implements MethodInterceptor
{

	protected static Logger log = Logger.getLogger(EncodingInterceptor.class.getName());
	private static final ThreadLocal<List<String>> encodingPool = new ThreadLocal<List<String>>();
	protected static final String codeStart = "<cn.gotom>";
	protected static final String codeEnd = "</cn.gotom>";

	public static void clear()
	{
		encodingPool.set(null);
	}

	private String origCoding;

	private String destCoding;

	public EncodingInterceptor()
	{

	}

	@Encoding
	private static class Internal
	{
	}

	protected Encoding readEncodingMetadata(MethodInvocation methodInvocation)
	{
		Encoding encoding;
		Method method = methodInvocation.getMethod();
		Class<?> targetClass = methodInvocation.getThis().getClass();

		encoding = method.getAnnotation(Encoding.class);
		if (null == encoding)
		{
			encoding = targetClass.getAnnotation(Encoding.class);
		}
		if (null == encoding)
		{
			encoding = Internal.class.getAnnotation(Encoding.class);
		}
		return encoding;
	}

	public EncodingInterceptor(String origCoding, String destCoding)
	{
		this.origCoding = origCoding;
		this.destCoding = destCoding;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable
	{
		if (StringUtils.isNotEmpty(origCoding) && StringUtils.isNotEmpty(destCoding))
		{
			int i = 0;
			for (Object arg : invocation.getArguments())
			{
				if (arg instanceof String)
				{
					invocation.getArguments()[i] = coding(arg.toString());
				}
				else
				{
					encoding(arg);
				}
				i++;
			}
		}
		return invocation.proceed();
	}

	public String getOrigCoding()
	{
		return origCoding;
	}

	public void setOrigCoding(String origCoding)
	{
		this.origCoding = origCoding;
	}

	public String getDestCoding()
	{
		return destCoding;
	}

	public void setDestCoding(String destCoding)
	{
		this.destCoding = destCoding;
	}

	public static boolean isWrapClass(Class<?> clz)
	{
		try
		{
			if (clz.isEnum())
			{
				return true;
			}
			if (((Class<?>) clz.getField("TYPE").get(null)).isPrimitive())
			{
				return true;
			}
		}
		catch (Exception e)
		{
			if (clz.getCanonicalName().startsWith("java.lang."))
			{
				return true;
			}
		}
		return false;
	}

	protected String coding(String value)
	{
		if (StringUtils.isNullOrEmpty(value))
		{
			return value;
		}
		if (value.getBytes().length == value.length())
		{
			return value;
		}
		String tmp = codeStart + StringUtils.encoding(value, this.getOrigCoding(), this.getDestCoding()) + codeEnd;
		// log.debug(value + " > " + tmp);
		return tmp;
	}

	protected void encoding(Object bean)
	{
		encoding(bean, true);
	}

	protected Object encoding(Object bean, boolean encodingChild)
	{
		if (bean == null)
			return bean;
		if (isWrapClass(bean.getClass()))
		{
			return bean;
		}
		if (bean instanceof Date)
		{
			return bean;
		}
		if (bean.getClass().isArray())
		{
			for (int i = 0; i < Array.getLength(bean); i++)
			{
				Object value = Array.get(bean, i);
				if (value != null && value instanceof String)
				{
					Array.set(bean, i, coding(value.toString()));
				}
				else if (value != null && !(value instanceof Date))
				{
					encodingProperty(value, encodingChild);
				}
			}
		}
		else if (bean instanceof Collection)
		{
			Collection<?> collection = (Collection<?>) bean;
			Iterator<?> iterator = collection.iterator();
			while (iterator.hasNext())
			{
				encodingProperty(iterator.next(), encodingChild);
			}
		}
		else if (bean instanceof Iterator)
		{
			Iterator<?> iterator = (Iterator<?>) bean;
			while (iterator.hasNext())
			{
				encodingProperty(iterator.next(), encodingChild);
			}
		}
		else if (bean instanceof Map)
		{
			Map<?, ?> map = (Map<?, ?>) bean;
			for (Object value : map.values())
			{
				encodingProperty(value, encodingChild);
			}
		}
		else
		{
			encodingProperty(bean, encodingChild);
		}
		return bean;
	}

	protected void encodingProperty(Object bean, boolean encodingChild)
	{
		String key = (bean.toString() + "{" + origCoding + ">" + destCoding + "}");
		log.info(key);
		// if (contains(key))
		// {
		// return;
		// }
		PropertyDescriptor[] ps = null;
		try
		{
			ps = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
		}
		catch (IntrospectionException e)
		{
			e.printStackTrace();
		}
		if (ps == null)
			return;
		for (PropertyDescriptor pd : ps)
		{
			if (pd.getWriteMethod() != null)
			{
				try
				{
					Object value = pd.getReadMethod().invoke(bean, new Object[] {});
					if (value != null && value instanceof String)
					{
						pd.getWriteMethod().invoke(bean, new Object[] { coding(value.toString()) });
					}
					else if (encodingChild && value != null)
					{
						pd.getWriteMethod().invoke(bean, new Object[] { encoding(value, false) });
					}
				}
				catch (Exception e)
				{
					log.error(bean + "-" + pd.getName());
					e.printStackTrace();
				}
			}
		}
	}

	protected boolean contains(String key)
	{
		List<String> currentList = encodingPool.get();
		if (currentList == null)
		{
			currentList = new ArrayList<String>();
			encodingPool.set(currentList);
		}
		if (currentList.contains(key))
		{
			return true;
		}
		else
		{
			currentList.add(key);
		}
		return false;
	}

	public static void encodingCallback(Object bean, String origCoding, String destCoding)
	{
		if (bean == null)
			return;
		if (isWrapClass(bean.getClass()))
		{
			return;
		}
		System.out.println(bean + " " + origCoding + " to " + destCoding);
		if (bean.getClass().isArray())
		{
			for (int i = 0; i < Array.getLength(bean); i++)
			{
				Object value = Array.get(bean, i);
				if (value != null && value instanceof String)
				{
					Array.set(bean, i, StringUtils.encoding(value.toString(), origCoding, destCoding));
				}
				else if (value != null)
				{
					encodingCallback(value, origCoding, destCoding);
				}
			}
			System.out.println(bean.getClass());
		}
		else if (bean instanceof Collection)
		{
			Collection<?> collection = (Collection<?>) bean;
			while (collection.iterator().hasNext())
			{
				encodingCallback(collection.iterator().next(), origCoding, destCoding);
			}
		}
		else if (bean instanceof Iterator)
		{
			Iterator<?> iterator = (Iterator<?>) bean;
			while (iterator.hasNext())
			{
				encodingCallback(iterator.next(), origCoding, destCoding);
			}
		}
		else if (bean instanceof Map)
		{
			Map<?, ?> map = (Map<?, ?>) bean;
			for (Object value : map.values())
			{
				encodingCallback(value, origCoding, destCoding);
			}
		}
		else
		{
			try
			{
				PropertyDescriptor[] ps = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
				for (PropertyDescriptor pd : ps)
				{
					try
					{
						Object value = pd.getReadMethod().invoke(bean, new Object[] {});
						if (value != null && value instanceof String)
						{
							value = StringUtils.encoding(value.toString(), origCoding, destCoding);
							pd.getWriteMethod().invoke(bean, new Object[] { value });
						}
						else
						{
							encodingCallback(value, origCoding, destCoding);
						}
					}
					catch (IllegalArgumentException e)
					{
						e.printStackTrace();
					}
					catch (IllegalAccessException e)
					{
						e.printStackTrace();
					}
					catch (InvocationTargetException e)
					{
						e.printStackTrace();
					}

				}
			}
			catch (IntrospectionException e)
			{
				e.printStackTrace();
			}
		}
	}
}
