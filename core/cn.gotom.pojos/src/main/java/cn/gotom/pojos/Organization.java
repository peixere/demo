package cn.gotom.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "sort", nullable = false)
	private int sort;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "custom_id", referencedColumnName = "id")
	private Custom custom;

	@ManyToMany
	@JoinTable(name = "core_org_user", joinColumns = { @JoinColumn(name = "org_id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "user_id", nullable = false) })
	private java.util.List<User> users;
	
	public String getParentId()
	{
		return parentId;
	}

	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
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

	public java.util.List<User> getUsers()
	{
		return users;
	}

	public void setUsers(java.util.List<User> users)
	{
		this.users = users;
	}

}
