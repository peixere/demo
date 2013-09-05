package cn.gotom.pojos;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * 
 * 实体表基类
 * 
 * @author peixere@qq.com
 * 
 * @version 2012-12-03
 * 
 */
@MappedSuperclass
public abstract class SuperEntity implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id()
	@Column(name = "id", nullable = false, columnDefinition = "char(36)", length = 36)
	private String id = UUID.randomUUID().toString();

	@Column(name = "version_num", nullable = false)
	private long versionNum = System.currentTimeMillis();

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_create", updatable = false)
	private Date dateCreate = new Date();

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_update", nullable = false)
	private Date dateUpdate = new Date();

	@Transient
	private boolean checked;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public long getVersionNum()
	{
		return versionNum;
	}

	public void setVersionNum(long versionNum)
	{
		this.versionNum = versionNum;
	}

	public Date getDateCreate()
	{
		return dateCreate;
	}

	public void setDateCreate(Date dateCreate)
	{
		this.dateCreate = dateCreate;
	}

	public Date getDateUpdate()
	{
		return dateUpdate;
	}

	public void setDateUpdate(Date dateUpdate)
	{
		this.dateUpdate = dateUpdate;
	}

	public boolean isChecked()
	{
		return checked;
	}

	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		return (o != null && o.toString().equals(this.toString()));
	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode()
	{
		return (id != null ? id.hashCode() : 0);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{
		return getClass().getName() + "@id=" + this.id;
	}
}
