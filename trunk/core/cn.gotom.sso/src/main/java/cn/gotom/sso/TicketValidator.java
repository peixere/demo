package cn.gotom.sso;


public interface TicketValidator
{
	public static final String Method = "method";
	public static final String Login = "login";
	public static final String Logout = "logout";
	public static final String Code = "code";
	public static final String Validate = "validate";
	Ticket validate(String ticketName, String service) throws SSOException;
}
