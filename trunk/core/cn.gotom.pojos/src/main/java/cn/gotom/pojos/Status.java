package cn.gotom.pojos;

/**
 * 
 * 状态
 * 
 * @author peixere@qq.com
 * 
 * @version 2012-12-03
 * 
 * 0:正常;1:挂起;2:删除
 * 
 */
public enum Status
{
	Normal(0), Banned(1), Delete(2);
	private int value = 0;

	Status(int value)
	{
		this.value = value;
	}

	public int value()
	{
		return this.value;
	}

	public static Status valueOf(int value)
	{
		for (Status v : values())
		{
			if (v.value() == value)
			{
				return v;
			}
		}
		return Normal;
	}
}
