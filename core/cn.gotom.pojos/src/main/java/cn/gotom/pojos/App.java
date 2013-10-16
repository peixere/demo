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

	@Column(unique = true, nullable = false, length = 100)
	private String name;

	@Column(unique = true, nullable = false, length = 200)
	private String description;

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