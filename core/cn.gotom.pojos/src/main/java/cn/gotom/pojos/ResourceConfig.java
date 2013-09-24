package cn.gotom.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * 资源信息
 * 
 * @author peixere@qq.com
 * 
 * @version 2012-12-03
 * 
 */
@Entity
@Table(name = "core_resource_config")
public class ResourceConfig extends SuperEntity implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Column(unique = true, nullable = false, length = 50)
	private String name;

	@Column(name = "res_value", nullable = false, length = 200)
	private String resValue;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getResValue()
	{
		return resValue;
	}

	public void setResValue(String resValue)
	{
		this.resValue = resValue;
	}
}