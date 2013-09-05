package cn.gotom.beancoding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.beanutils.BeanUtils;

import cn.gotom.util.StringUtils;

public class DecodingInterceptor extends EncodingInterceptor
{
	public DecodingInterceptor()
	{

	}

	public DecodingInterceptor(String origCoding, String destCoding)
	{
		super(origCoding, destCoding);
	}

	protected String coding(String value)
	{
		if (StringUtils.isNullOrEmpty(value))
		{
			return value;
		}
		if (value.startsWith(codeStart) && value.endsWith(codeEnd))
		{
			value = value.replace(codeStart, "").replace(codeEnd, "");
			String tmp = StringUtils.encoding(value, this.getOrigCoding(), this.getDestCoding());
			value = tmp;
		}
		return value;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable
	{
		Object res = invocation.proceed();
		if (StringUtils.isNotEmpty(this.getOrigCoding()) && StringUtils.isNotEmpty(this.getDestCoding()))
		{
			res = encoding(res, true);
		}
		return res;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
				else if (value != null)
				{
					try
					{
						Object dest = BeanUtils.cloneBean(value);
						encodingProperty(dest, encodingChild);
						Array.set(bean, i, dest);
					}
					catch (Exception e)
					{

					}
				}
			}
		}
		else if (bean instanceof Collection)
		{
			Collection<?> collection = (Collection<?>) bean;
			Collection newList = new ArrayList();
			Iterator<?> iterator = collection.iterator();
			while (iterator.hasNext())
			{
				try
				{
					Object dest = BeanUtils.cloneBean(iterator.next());
					encodingProperty(dest, encodingChild);
					newList.add(dest);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				// encodingProperty(iterator.next(), encodingChild);
			}
			collection.clear();
			collection.addAll(newList);
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
			Map map = (Map) bean;
			for (Object key : map.keySet())
			{
				Object value = map.get(key);
				try
				{
					Object dest = BeanUtils.cloneBean(value);
					encodingProperty(dest, encodingChild);
					map.put(key, dest);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			try
			{
				Object dest = BeanUtils.cloneBean(bean);
				encodingProperty(dest, encodingChild);
				bean = dest;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return bean;
	}

}
