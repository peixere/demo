package cn.gotom.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * 
 * 功能菜单资源信息表
 * 
 * @author peixere@qq.com
 * 
 * @version 2012-12-03
 * 
 */
@Entity
@Table(name = "core_right")
public class Right extends SuperEntity implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Column(name = "parent_id", nullable = true, columnDefinition = "char(36)", length = 36)
	private String parentId;

	@Column(name = "text", nullable = false, length = 50)
	private String text;

	@Column(name = "sort", nullable = false)
	private int sort;

	@Column(name = "icon_cls", nullable = true, length = 50)
	private String iconCls;

	@Column(name = "type", nullable = true, length = 50)
	private String type;

	@Column(name = "component", nullable = false, length = 300)
	private String component;

	@Column()
	private Boolean leaf;

	@Column(name = "resource", nullable = false, length = 300)
	private String resource;

	/**
	 * 关联app
	 */
	@Column(name = "app_code", nullable = true, length = 100)
	private String appCode = App.ROOT;

	@ManyToMany()
	@JoinTable(name = "core_role_right", joinColumns = { @JoinColumn(name = "right_id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "role_id", nullable = false) })
	private java.util.List<Role> roles;

	public Right()
	{
	}

	public String getParentId()
	{
		return parentId;
	}

	public void setParentId(String parentId)
	{
		this.parentId = parentId;
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

	public String getIconCls()
	{
		return iconCls;
	}

	public void setIconCls(String iconCls)
	{
		this.iconCls = iconCls;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getComponent()
	{
		return component;
	}

	public void setComponent(String component)
	{
		this.component = component;
	}

	public String getResource()
	{
		return resource;
	}

	public Boolean getLeaf()
	{
		return leaf;
	}

	public void setLeaf(Boolean leaf)
	{
		this.leaf = leaf;
	}

	public void setResource(String resource)
	{
		this.resource = resource;
	}

	public String getAppCode()
	{
		return appCode;
	}

	public void setAppCode(String appCode)
	{
		this.appCode = appCode;
	}

	public java.util.List<Role> getRoles()
	{
		return this.roles;
	}

	public void setRoles(java.util.List<Role> roles)
	{
		this.roles = roles;
	}

}