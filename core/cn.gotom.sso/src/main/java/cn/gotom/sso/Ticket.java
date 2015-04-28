package cn.gotom.sso;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public interface Ticket extends Serializable
{
	public static String DateFromat = "yyyy-MM-dd HH:mm:ss.SSS";

	String getId();

	String getUser();

	String getServiceUrl();

	Date getCreateDate();

	Date getValidFromDate();

	Date getValidUntilDate();

	String getRedirect();

	Map<String, Object> getAttributes();

	boolean getSuccess();

	String getMessage();

	void setMessage(String message);

	String toJSON();
}
