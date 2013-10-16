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

	/**
	 * 接入应用的唯一编码
	 */
	@Column(unique = true, name = "app_code", nullable = false, length = 100)
	private String appCode;

	/**
	 * 应用名称
	 */
	@Column(nullable = false, length = 100)
	private String name;

	/**
	 * 接入应用servletPath前的域名和应用目录部份 如：http://localhost:8080/app
	 */
	@Column(name = "context_path", nullable = false, length = 200)
	private String contextPath;

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

	public String getContextPath()
	{
		return contextPath;
	}

	public void setContextPath(String contextPath)
	{
		this.contextPath = contextPath;
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