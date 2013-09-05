package cn.gotom.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * 企业客户
 * 
 * @author peixere@qq.com
 * 
 */
@Entity
@Table(name = "core_custom")
public class Custom extends SuperEntity implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(unique = true, nullable = false, length = 100)
	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User creater;

	@ManyToMany
	@JoinTable(name = "core_custom_right", joinColumns = { @JoinColumn(name = "custom_id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "right_id", nullable = false) })
	private java.util.List<Right> rights;

	@OneToMany(mappedBy = "custom", fetch = FetchType.LAZY)
	private java.util.List<Role> roles;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public User getCreater()
	{
		return creater;
	}

	public void setCreater(User creater)
	{
		this.creater = creater;
	}

	public java.util.List<Right> getRights()
	{
		return rights;
	}

	public void setRights(java.util.List<Right> rights)
	{
		this.rights = rights;
	}

	public java.util.List<Role> getRoles()
	{
		return roles;
	}

	public void setRoles(java.util.List<Role> roles)
	{
		this.roles = roles;
	}

}
