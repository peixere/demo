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

	@Column(name = "option_code", nullable = false, length = 100)
	private String optionCode;

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

	public String getOptionCode()
	{
		return optionCode;
	}

	public void setOptionCode(String optionCode)
	{
		this.optionCode = optionCode;
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