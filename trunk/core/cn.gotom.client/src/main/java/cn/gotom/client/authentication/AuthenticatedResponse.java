package cn.gotom.client.authentication;

public class AuthenticatedResponse extends AuthenticatedRequest
{
	private int status;

	private String message;

	public AuthenticatedResponse()
	{

	}

	public AuthenticatedResponse(AuthenticatedRequest request)
	{
		this.setAppCode(request.getAppCode());
		this.setUrl(request.getUrl());
		this.setUsername(request.getUsername());
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
}
