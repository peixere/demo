package cn.gotom.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name = "core_version_log")
public class VersionLog extends SuperEntity implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Note("实体ID")
	@Column(name = "entity_id", nullable = false, columnDefinition = "char(36)", length = 36)
	private String entityId;

	@Note("实体名")
	@Column(name = "entity_clazz", nullable = false, length = 18)
	private String entityClazz;

	@Note("实体操作标识，0：保存；1：删除")
	@Column(name = "entity_ctrl")
	private int entityCtrl;

	@Transient
	private Object entity;

	public VersionLog()
	{

	}

	public String getEntityId()
	{
		return entityId;
	}

	public void setEntityId(String entityId)
	{
		this.entityId = entityId;
	}

	public String getEntityClazz()
	{
		return entityClazz;
	}

	public void setEntityClazz(String entityClazz)
	{
		this.entityClazz = entityClazz;
	}

	public int getEntityCtrl()
	{
		return entityCtrl;
	}

	public void setEntityCtrl(int entityCtrl)
	{
		this.entityCtrl = entityCtrl;
	}

	public Object getEntity()
	{
		return entity;
	}

	public void setEntity(Object entity)
	{
		this.entity = entity;
	}

}