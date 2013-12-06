package cn.gotom.sso;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public interface Ticket extends Serializable
{

	String getName();

	String getUser();

	String getServiceUrl();

	Date getCreateDate();
	
	Date getValidFromDate();

	Date getValidUntilDate();

	Map<String, Object> getAttributes();
}
