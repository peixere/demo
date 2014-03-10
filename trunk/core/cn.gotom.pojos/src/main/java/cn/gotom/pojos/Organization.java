package cn.gotom.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * 组织架构
 * 
 * @author peixere@qq.com
 * 
 * @version 2013-08-13
 * 
 */
@Entity
@Table(name = "core_organization")
public class Organization extends SuperEntity implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "parent_id", nullable = true, columnDefinition = "char(36)", length = 36)
	private String parentId;

	@Column(name = "code", nullable = false, length = 50)
	private String code;

	@Column(name = "text", nullable = false, length = 100)
	private String text;

	@Column(name = "sort", nullable = false)
	private int sort;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "custom_id", referencedColumnName = "id")
	private Custom custom;

	@Transient
	private java.util.List<Organization> children;

	public String getParentId()
	{
		return parentId;
	}

	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public int getSort()
	{
		return sort;
	}

	public void setSort(int sort)
	{
		this.sort = sort;
	}

	public Custom getCustom()
	{
		return custom;
	}

	public void setCustom(Custom custom)
	{
		this.custom = custom;
	}

	public java.util.List<Organization> getChildren()
	{
		return children;
	}

	public void setChildren(java.util.List<Organization> children)
	{
		this.children = children;
	}

}
