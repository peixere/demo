package cn.gotom.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "core_org_user", joinColumns = { @JoinColumn(name = "org_id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "user_id", nullable = false) })
	private java.util.List<User> users;

	@Column(name = "custom_id", nullable = false, columnDefinition = "char(36)", length = 36)
	private String customId = Custom.Default;

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

	public java.util.List<User> getUsers()
	{
		return users;
	}

	public void setUsers(java.util.List<User> users)
	{
		this.users = users;
	}

	public java.util.List<Organization> getChildren()
	{
		return children;
	}

	public void setChildren(java.util.List<Organization> children)
	{
		this.children = children;
	}

	public String getCustomId()
	{
		return customId;
	}

	public void setCustomId(String customId)
	{
		this.customId = customId;
	}


}
