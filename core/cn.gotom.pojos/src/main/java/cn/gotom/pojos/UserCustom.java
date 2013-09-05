package cn.gotom.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "core_user_custom")
public class UserCustom extends SuperEntity implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "custom_id", referencedColumnName = "id")
	private Custom custom;

	/**
	 * 是否为默认登录公司
	 */
	@Column(name = "default_flag", nullable = true, length = 1)
	private boolean defaultFlag;

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public Custom getCustom()
	{
		return custom;
	}

	public void setCustom(Custom custom)
	{
		this.custom = custom;
	}

	public boolean isDefaultFlag()
	{
		return defaultFlag;
	}

	public void setDefaultFlag(boolean defaultFlag)
	{
		this.defaultFlag = defaultFlag;
	}

}
