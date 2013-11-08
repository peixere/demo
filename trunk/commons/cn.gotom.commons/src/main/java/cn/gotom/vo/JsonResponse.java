package cn.gotom.vo;

public class JsonResponse
{

	private boolean success = true;

	private int status = 200;

	private String statusText = "OK";

	private Object responseText;

	public boolean getSuccess()
	{
		return success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getStatusText()
	{
		return statusText;
	}

	public void setStatusText(String statusText)
	{
		this.statusText = statusText;
	}

	public Object getResponseText()
	{
		return responseText;
	}

	public void setResponseText(Object responseText)
	{
		this.responseText = responseText;
	}

}
