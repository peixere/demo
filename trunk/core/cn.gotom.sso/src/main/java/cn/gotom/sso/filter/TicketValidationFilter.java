package cn.gotom.sso.filter;

import cn.gotom.sso.SSOException;
import cn.gotom.sso.Ticket;
import cn.gotom.sso.TicketValidator;

public class TicketValidationFilter extends AuthenticationFilter implements TicketValidator
{

	@Override
	public Ticket validate(String ticketName, String service) throws SSOException
	{
		// TODO Auto-generated method stub
		return null;
	}

}
