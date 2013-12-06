package cn.gotom.sso;


public interface TicketValidator
{
	Ticket validate(String ticketName, String service) throws SSOException;
}
