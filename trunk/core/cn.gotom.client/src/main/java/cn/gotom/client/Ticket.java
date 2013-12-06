package cn.gotom.client;

import java.io.Serializable;
import java.util.Date;

import cn.gotom.client.util.CommonUtils;

public class Ticket implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String CONST_TICKET = "CONST_TICKET";
	private String name;
	private String url;
	private String user;
	private Date createDate = new Date();

	public Ticket(String name)
	{
		this.name = name;
		CommonUtils.assertNotNull(this.name, "name cannot be null.");
	}

	public String toString()
	{
		return getName();
	}

	public boolean equals(final Object o)
	{
		if (o == null)
		{
			return false;
		}
		else if (!(o instanceof Ticket))
		{
			return false;
		}
		else
		{
			return getName().equals(((Ticket) o).getName());
		}
	}

	public int hashCode()
	{
		return 37 * getName().hashCode();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getUser()
	{
		return user;
	}

	public void setUser(String user)
	{
		this.user = user;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

}
