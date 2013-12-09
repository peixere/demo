package cn.gotom.sso;

import java.util.Date;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;

import cn.gotom.sso.util.CommonUtils;

public class TicketImpl implements Ticket
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String id;
	private String serviceUrl;
	private String redirect;
	private String user;
	private Date createDate = new Date();
	private Date validFromDate = new Date();
	private Date validUntilDate = new Date();
	private boolean success;

	protected TicketImpl()
	{
		this("");
	}

	public TicketImpl(String id)
	{
		this.id = id;
		CommonUtils.assertNotNull(this.id, "id cannot be null.");
	}

	public String toString()
	{
		return getId();
	}

	public static TicketImpl parseFromJSON(String jsonString)
	{
		try
		{
			if (CommonUtils.isNotEmpty(jsonString))
			{
				JSON json = JSONSerializer.toJSON(jsonString);
				JsonConfig jsonConfig = new JsonConfig();
				jsonConfig.setRootClass(TicketImpl.class);
				return (TicketImpl) JSONSerializer.toJava(json, jsonConfig);
			}
			return null;
		}
		catch (Exception ex)
		{
			Logger.getLogger(Ticket.class).error("", ex);
			return null;
		}
	}

	@Override
	public String toJSON()
	{
		return JSONSerializer.toJSON(this).toString();
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
			return getId().equals(((TicketImpl) o).getId());
		}
	}

	public int hashCode()
	{
		return 37 * getId().hashCode();
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

	public void setRedirect(String redirect)
	{
		this.redirect = redirect;
	}

	@Override
	public boolean getSuccess()
	{
		return success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	@Override
	public String getId()
	{
		return this.id;
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

	@Override
	public String getRedirect()
	{
		return this.redirect;
	}

}
