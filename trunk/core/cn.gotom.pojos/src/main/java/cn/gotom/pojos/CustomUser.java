package cn.gotom.pojos;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "core_custom_user")
public class CustomUser extends SuperEntity implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final String Default = "88888888-8888-8888-8888-888888888888";

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "custom_id", referencedColumnName = "id")
	private Custom custom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	public Custom getCustom()
	{
		return custom;
	}

	public void setCustom(Custom custom)
	{
		this.custom = custom;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

}