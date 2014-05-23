package cn.gotom.pojos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
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
public abstract class SuperEntity implements IdSerializable
{
	private static final long serialVersionUID = 1L;

	public static final SimpleDateFormat millisecondformat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	public static String randomID()
	{
		return UUID.randomUUID().toString();
	}

	@Id()
	@Column(name = "id", nullable = false, columnDefinition = "char(36)", length = 36)
	private String id = randomID();

	@Column(name = "version_nom")
	private long versionNow;

	@Column(name = "version_create", updatable = false)
	private long versionCreate;

	@Transient
	private boolean selected;

	public SuperEntity()
	{
		try
		{
			versionCreate = versionNow = Long.parseLong(millisecondformat.format(new Date()));
		}
		catch (Exception ex)
		{
			ex.getMessage();
		}
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public long getVersionNow()
	{
		return versionNow;
	}

	public void setVersionNow(long versionNow)
	{
		this.versionNow = versionNow;
	}

	public long getVersionCreate()
	{
		return versionCreate;
	}

	public void setVersionCreate(long versionCreate)
	{
		this.versionCreate = versionCreate;
	}

	public boolean getSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
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
