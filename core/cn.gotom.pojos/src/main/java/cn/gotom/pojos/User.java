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
 * 用户信息表
 * 
 * @author peixere@qq.com
 * 
 * @version 2012-12-03
 * 
 */
@Entity
@Table(name = "core_user")
public class User extends SuperEntity implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final String ROOT = "root";

	public static final String CurrentLoginUser = "CurrentLoginUser";

	@Note("姓名")
	@Column(nullable = true, length = 50)
	private String name;

	@Column(nullable = false, length = 50)
	private String password;

	@Note("登录名")
	@Column(unique = true, nullable = false, length = 100, updatable = true)
	private String username;

	@Note("手机号码")
	@Column(length = 20)
	private String mobile;

	@Note("工作卡号")
	@Column(name = "card_rfid", length = 18)
	private String cardRFID;

	@Note("身份证号码")
	@Column(name = "card_id", length = 18)
	private String cardId;

	@Note("0:正常;1:挂起;2:删除")
	@Column(nullable = true, length = 11)
	private Status status = Status.Normal;

	@ManyToMany
	@JoinTable(name = "core_user_role", joinColumns = { @JoinColumn(name = "user_id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "role_id", nullable = false) })
	private java.util.List<Role> roles;

	@Column(name = "default_custom_id", nullable = false, columnDefinition = "char(36)", length = 36)
	private String defaultCustomId = Custom.Default;

	public User()
	{
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getCardRFID()
	{
		return cardRFID;
	}

	public void setCardRFID(String cardRFID)
	{
		this.cardRFID = cardRFID;
	}

	public String getCardId()
	{
		return cardId;
	}

	public void setCardId(String cardId)
	{
		this.cardId = cardId;
	}

	public Status getStatus()
	{
		return status;
	}

	public void setStatus(Status status)
	{
		this.status = status;
	}

	public java.util.List<Role> getRoles()
	{
		return roles;
	}

	public void setRoles(java.util.List<Role> roles)
	{
		this.roles = roles;
	}

	public String getDefaultCustomId()
	{
		return defaultCustomId;
	}

	public void setDefaultCustomId(String defaultCustomId)
	{
		this.defaultCustomId = defaultCustomId;
	}

}