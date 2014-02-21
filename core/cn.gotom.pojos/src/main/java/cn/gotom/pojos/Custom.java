package cn.gotom.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "core_custom")
public class Custom extends SuperEntity implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final String Default = "88888888-8888-8888-8888-888888888888";

	@Column(nullable = true, length = 250)
	private String name;

	@Column(length = 250)
	private String description;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "core_custom_user", joinColumns = { @JoinColumn(name = "custom_id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "user_id", nullable = false) })
	private java.util.List<User> users;

}