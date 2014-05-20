package cn.gotom.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * 客户（企业）信息表
 * 
 * @author peixere@qq.com
 * 
 * @version 2012-12-03
 * 
 */
@Entity
@Table(name = "core_custom")
public class Custom extends SuperEntity implements Serializable
{
	private static final long serialVersionUID = 1L;
	public static final String currentCustomId = "currentCustomId";
	public static final String Default = "88888888-8888-8888-8888-888888888888";

	@Column(nullable = true, length = 250)
	private String name;
	@Column(length = 250)
	private String titlename;
	@Column(length = 250)
	private String description;
	@Column(length = 250)
	private String fontStyle;
	@Column(length = 250)
	private String logoUrl;
	@Column(length = 250)
	private String topbgUrl;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getFontStyle()
	{
		return fontStyle;
	}

	public void setFontStyle(String fontStyle)
	{
		this.fontStyle = fontStyle;
	}

	public String getLogoUrl()
	{
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl)
	{
		this.logoUrl = logoUrl;
	}

	public String getTopbgUrl()
	{
		return topbgUrl;
	}

	public void setTopbgUrl(String topbgUrl)
	{
		this.topbgUrl = topbgUrl;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getTitlename()
	{
		return titlename;
	}

	public void setTitlename(String titlename)
	{
		this.titlename = titlename;
	}

}