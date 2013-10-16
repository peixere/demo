package cn.gotom.auth.client;

public class Authenticated
{
	private String username;

	private String appCode;

	private String url;

	private int response;

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

	public int getResponse()
	{
		return response;
	}

	public void setResponse(int response)
	{
		this.response = response;
	}
}
