package cn.gotom.sso;

import java.util.Date;
import java.util.Map;

import cn.gotom.sso.util.CommonUtils;

public class TicketImpl implements Ticket
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String serviceUrl;
	private String user;
	private Date createDate = new Date();
	private Date validFromDate;
	private Date validUntilDate;

	public TicketImpl(String name)
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
		else if (!(o instanceof TicketImpl))
		{
			return false;
		}
		else
		{
			return getName().equals(((TicketImpl) o).getName());
		}
	}

	public int hashCode()
	{
		return 37 * getName().hashCode();
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setServiceUrl(String serviceUrl)
	{
		this.serviceUrl = serviceUrl;
	}

	public void setUser(String user)
	{
		this.user = user;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public void setValidFromDate(Date validFromDate)
	{
		this.validFromDate = validFromDate;
	}

	public void setValidUntilDate(Date validUntilDate)
	{
		this.validUntilDate = validUntilDate;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public String getUser()
	{
		return this.user;
	}

	@Override
	public String getServiceUrl()
	{
		return this.serviceUrl;
	}

	@Override
	public Date getCreateDate()
	{
		return createDate;
	}

	@Override
	public Date getValidFromDate()
	{
		return this.validFromDate;
	}

	@Override
	public Date getValidUntilDate()
	{
		return this.validUntilDate;
	}

	@Override
	public Map<String, Object> getAttributes()
	{
		return null;
	}

}
