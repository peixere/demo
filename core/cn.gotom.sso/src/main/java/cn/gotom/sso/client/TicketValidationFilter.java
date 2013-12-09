package cn.gotom.sso.client;

import cn.gotom.sso.SSOException;
import cn.gotom.sso.Ticket;
import cn.gotom.sso.TicketImpl;
import cn.gotom.sso.TicketValidator;
import cn.gotom.sso.util.CommonUtils;

public class TicketValidationFilter extends AuthenticationFilter implements TicketValidator
{

	@Override
	public Ticket validate(String ticketId, String service) throws SSOException
	{
		String queryString = TicketValidator.Method + "=" + TicketValidator.Validate + "&" + this.getTicketParameterName() + "=" + ticketId;
		String url = service.indexOf("?") >= 0 ? "&" : "?" + queryString;
		String jsonString = CommonUtils.getResponseFromServer(url, "utf-8");
		Ticket ticket = TicketImpl.parseFromJSON(jsonString);
		return ticket;
	}
}
