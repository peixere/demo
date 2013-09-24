package cn.gotom.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * 选项配置
 * 
 * @author peixere@qq.com
 * 
 * @version 2013-09-24
 * 
 */
@Entity
@Table(name = "core_options_config")
public class OptionsConfig extends SuperEntity implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(name = "optionName", nullable = false, length = 100)
	private String optionName;

	@Column(name = "option_value", nullable = false, length = 100)
	private String optionValue;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getOptionName()
	{
		return optionName;
	}

	public void setOptionName(String optionName)
	{
		this.optionName = optionName;
	}

	public String getOptionValue()
	{
		return optionValue;
	}

	public void setOptionValue(String optionValue)
	{
		this.optionValue = optionValue;
	}

}