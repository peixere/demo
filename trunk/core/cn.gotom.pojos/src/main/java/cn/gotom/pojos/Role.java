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
import javax.persistence.Transient;

/**
 * 
 * 角色权限信息表
 * 
 * @author peixere@qq.com
 * 
 * @version 2012-12-03
 * 
 */
@Entity
@Table(name = "core_role")
public class Role extends SuperEntity implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false)
	private int sort;

	@ManyToMany
	@JoinTable(name = "core_role_right", joinColumns = { @JoinColumn(name = "role_id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "right_id", nullable = false) })
	private java.util.List<Right> rights;

	@ManyToMany
	@JoinTable(name = "core_user_role", joinColumns = { @JoinColumn(name = "role_id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "user_id", nullable = false) })
	private java.util.List<User> users;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", referencedColumnName = "id")
	private Organization organization;

	@Transient
	private String organizationId;

	public Role()
	{
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getSort()
	{
		return this.sort;
	}

	public void setSort(int sort)
	{
		this.sort = sort;
	}

	public java.util.List<Right> getRights()
	{
		return this.rights;
	}

	public void setRights(java.util.List<Right> rights)
	{
		this.rights = rights;
	}

	public java.util.List<User> getUsers()
	{
		return this.users;
	}

	public void setUsers(java.util.List<User> users)
	{
		this.users = users;
	}

	public Organization getOrganization()
	{
		return organization;
	}

	public void setOrganization(Organization organization)
	{
		this.organization = organization;
	}

	public String getOrganizationId()
	{
		return organizationId;
	}

	public void setOrganizationId(String organizationId)
	{
		this.organizationId = organizationId;
	}

}