package cn.gotom.pojos;

import java.util.ArrayList;
import java.util.List;

public class RightType
{
	public final static String URL = "URL";
	public final static String DIR = "DIR";
	public final static String COMPONENT = "COMPONENT";
	public final static String PLUGIN = "PLUGIN";
	public final static String PORTLET = "PORTLET";
	public final static String RESOURCE = "RESOURCE";

	private final static RightType[] typeArray;

	static
	{
		typeArray = new RightType[] { new RightType("菜单目录", DIR), new RightType("连接菜单", URL), new RightType("控件菜单", COMPONENT), new RightType("插件菜单", PLUGIN), new RightType("门户菜单", PORTLET), new RightType("资源权限", RESOURCE) };
	}

	public static List<RightType> getTypeList()
	{
		List<RightType> types = new ArrayList<RightType>();
		for (RightType type : typeArray)
		{
			types.add(type);
		}
		return types;
	}

	public RightType(String name, String value)
	{
		this.name = name;
		this.value = value;
	}

	private String name;
	private String value;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

}
