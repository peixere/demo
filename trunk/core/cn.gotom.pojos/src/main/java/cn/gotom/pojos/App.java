package cn.gotom.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * 接入应用
 * 
 * @author peixere@qq.com
 * 
 * @version 2013-09-24
 * 
 */
@Entity
@Table(name = "core_app")
public class App extends SuperEntity implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final String local = "local";

	@Column(unique = true, name = "app_code", nullable = false, length = 100)
	private String appCode;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(unique = true, nullable = false, length = 200)
	private String description;

	public String getAppCode()
	{
		return appCode;
	}

	public void setAppCode(String appCode)
	{
		this.appCode = appCode;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

}