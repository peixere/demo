package cn.gotom.sso.authentication;

import net.sf.json.JSONSerializer;

public class AuthenticatedRequest
{
	private String username;

	private String appCode;

	private String url;

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getAppCode()
	{
		return appCode;
	}

	public void setAppCode(String appCode)
	{
		this.appCode = appCode;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String toString()
	{
		return JSONSerializer.toJSON(this).toString();
	}
}
