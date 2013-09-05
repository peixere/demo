package cn.gotom.service;

public class Parameter<T>
{	
	public String name;
	public T value;

	public Parameter(String name, T value)
	{
		this.setName(name);
		this.setValue(value);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public T getValue()
	{
		return value;
	}

	public void setValue(T value)
	{
		this.value = value;
	}

}
