package cn.gotom.pojos;

public class Resource
{

	private Integer id;

	private String text;

	private String component;

	private String description;

	private String type;

	private String iconCls;

	private Integer sort;

	private Boolean leaf;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getComponent()
	{
		return component;
	}

	public void setComponent(String component)
	{
		this.component = component;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getIconCls()
	{
		return iconCls;
	}

	public void setIconCls(String iconCls)
	{
		this.iconCls = iconCls;
	}

	public Integer getSort()
	{
		return sort;
	}

	public void setSort(Integer sort)
	{
		this.sort = sort;
	}

	public Boolean getLeaf()
	{
		return leaf;
	}

	public void setLeaf(Boolean leaf)
	{
		this.leaf = leaf;
	}

}
