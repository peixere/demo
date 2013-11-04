package cn.gotom.servlet;

public class TreeModel
{
	private String id;
	private int sort;
	private String text;
	private String iconCls;
	private boolean leaf;
	private String type;
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public int getSort()
	{
		return sort;
	}
	public void setSort(int sort)
	{
		this.sort = sort;
	}
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}
	public String getIconCls()
	{
		return iconCls;
	}
	public void setIconCls(String iconCls)
	{
		this.iconCls = iconCls;
	}
	public boolean isLeaf()
	{
		return leaf;
	}
	public void setLeaf(boolean leaf)
	{
		this.leaf = leaf;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	
	
}
