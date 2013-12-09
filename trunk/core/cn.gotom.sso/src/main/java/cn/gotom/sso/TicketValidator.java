package cn.gotom.sso;


public interface TicketValidator
{
	public static final String Method = "muthod";
	public static final String Login = "login";
	public static final String Logout = "logout";
	public static final String Validate = "validate";
	Ticket validate(String ticketName, String service) throws SSOException;
}
