package cn.gotom.commons;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Clone implements Cloneable
{
	public synchronized void copyTo(Object dest)
	{
		Field[] fields = getClass().getFields();
		for (Field fs : fields)
		{
			Object value = null;
			try
			{
				value = fs.get(this);
				Field fd = dest.getClass().getDeclaredField(fs.getName());
				fd.set(dest, value);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		Method[] medhods = getClass().getMethods();
		Method[] destMds = dest.getClass().getMethods();
		for (Method ms : medhods)
		{
			Object value = null;
			String name = ms.getName();
			try
			{
				if (ms.getParameterTypes().length == 0 && name.startsWith("get") && !name.equals("getClass"))
				{
					value = ms.invoke(this, new Object[0]);
					String setName = "set" + name.substring(3);
					for (Method destMs : destMds)
					{
						if (setName.equals(destMs.getName()) && destMs.getParameterTypes()[0].equals(value.getClass()))
						{
							destMs.invoke(dest, new Object[] { value });
							break;
						}
					}
					// Method md = dest.getClass().getMethod(setName);
					// md.invoke(dest, new Object[] { value });
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public synchronized Object clone()
	{
		try
		{
			return super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
