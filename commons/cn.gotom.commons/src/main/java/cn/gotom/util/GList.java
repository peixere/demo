package cn.gotom.util;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

import cn.gotom.annotation.Description;

@Description("Generic")
public class GList<E> extends ArrayList<E>
{

	public static void main(String[] args)
	{
		GList<String> stringList = new GList<String>(String.class);
		stringList.asArray();
		GList<Byte> sList = new GList<Byte>(Byte.class);
		sList.asArray();
		sList.add((byte) 0xF0);
		sList.add((byte) 0xF1);
		Byte[] bytes = new Byte[3];
		bytes[0] = (byte) 0xF3;
		bytes[1] = 0x02;
		bytes[2] = 0x03;
		sList.AddRange(bytes);
		sList.RemoveRange(2, 2);
		sList.InsertRange(2, bytes);
		bytes = sList.asArray();
	}

	private static final long serialVersionUID = -5372580678501198681L;

	protected Class<?> genericsClass;

	public GList(Class<E> cls)
	{
		this();
		genericsClass = cls;
	}

	public GList()
	{
		super();
	}

	@Description("转成数据")
	public synchronized E[] asArray()
	{
		if (genericsClass == null)
		{
			if (this.size() > 0)
			{
				@SuppressWarnings("unchecked")
				E[] array = (E[]) Array.newInstance(this.get(0).getClass(), this.size());
				return this.toArray(array);
			}
			else
			{
				Class<?> clazz = ReflectionUtils.getClassGenricType(this.getClass());
				return null;
			}
		}
		else
		{
			@SuppressWarnings("unchecked")
			E[] array = (E[]) Array.newInstance(genericsClass, this.size());
			return this.toArray(array);
		}
	}

	@Description("删除集合中的数据")
	public synchronized void RemoveRange(int index, int len)
	{
		this.removeRange(index, index + len);
	}

	@Description("添加数据到集合中")
	public synchronized void InsertRange(int index, E[] array)
	{
		for (E e : array)
		{
			this.add(e);
		}
		for (int i = (this.size() - array.length - 1); i >= index; i--)
		{
			this.set(i + array.length, this.get(i));
		}
		for (int i = 0; i < array.length; i++)
		{
			this.set(i + index, array[i]);
		}
	}

	@Description("添加数据到集合中")
	public synchronized void AddRange(E[] array)
	{
		for (int i = 0; i < array.length; i++)
		{
			this.add(array[i]);
		}
	}
}
