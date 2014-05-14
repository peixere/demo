package cn.gotom.sso.client;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class LicenseFilter extends AuthenticationFilter
{
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(2015, 1, 1, 1, 1, 1);
		calendar.getTime().getTime();
		Date now = new Date();
		if (now.getTime() > calendar.getTime().getTime())
		{
			super.doFilter(request, response, filterChain);
		}
		else
		{
			request.getRequestDispatcher("/WEB-INF/sso/500.jsp").forward(request, response);
		}
	}
}
