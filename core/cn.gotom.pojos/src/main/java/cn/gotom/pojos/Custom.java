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
	@Column(name = "logo_id", columnDefinition = "char(36)", length = 36)
	private String logoId;
	@Column(name = "topbg_id", columnDefinition = "char(36)", length = 36)
	private String topbgId;
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getTitlename()
	{
		return titlename;
	}
	public void setTitlename(String titlename)
	{
		this.titlename = titlename;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getFontStyle()
	{
		return fontStyle;
	}
	public void setFontStyle(String fontStyle)
	{
		this.fontStyle = fontStyle;
	}
	public String getLogoId()
	{
		return logoId;
	}
	public void setLogoId(String logoId)
	{
		this.logoId = logoId;
	}
	public String getTopbgId()
	{
		return topbgId;
	}
	public void setTopbgId(String topbgId)
	{
		this.topbgId = topbgId;
	}

}