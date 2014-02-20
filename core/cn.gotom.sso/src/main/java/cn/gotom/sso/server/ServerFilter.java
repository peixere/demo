package cn.gotom.sso.server;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.gotom.sso.Ticket;
import cn.gotom.sso.TicketImpl;
import cn.gotom.sso.TicketMap;
import cn.gotom.sso.TicketValidator;
import cn.gotom.sso.filter.AbstractCommonFilter;
import cn.gotom.sso.util.CommonUtils;
import cn.gotom.sso.util.PasswordEncoder;
import cn.gotom.sso.util.PasswordEncoderMessageDigest;
import cn.gotom.sso.util.UrlUtils;

public class ServerFilter extends AbstractCommonFilter
{
	private static final String sqlPropertyName = "loginsql";
	protected SSOService ssoService;
	protected PasswordEncoder passwordEncoder;
	private String loginSQL;
	private String encodingAlgorithm;
	private String loginPath;
	private String logoutPath;
	private String successPath;
	private String errorMsg = "登录失败，请检查你的用户名或密码是否正确！";

	protected void initService()
	{
		ssoService = new SSOServiceImpl();
		passwordEncoder = new PasswordEncoderMessageDigest(encodingAlgorithm);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		super.init(filterConfig);
		encodingAlgorithm = this.getInitParameter(filterConfig, "encodingAlgorithm", "MD5");
		loginPath = this.getInitParameter(filterConfig, "login", "/WEB-INF/sso/login.jsp");
		logoutPath = this.getInitParameter(filterConfig, "logout", loginPath);
		successPath = this.getInitParameter(filterConfig, "success", null);
		loginSQL = this.getInitParameter(filterConfig, sqlPropertyName, "select password from core_user where username=?");
		setServerLoginUrl(getInitParameter(filterConfig, serverLoginUrlParameter, null));
		if (CommonUtils.isNotEmpty(serverLoginUrl) && serverLoginUrl.startsWith(contextPath))
		{
			setServerLoginUrl(serverLoginUrl.substring(contextPath.length(), serverLoginUrl.length()));
			log.info("Property [serverLoginUrl] value [" + serverLoginUrl + "]");
		}
		CommonUtils.assertNotNull(this.getServerLoginUrl(), serverLoginUrlParameter + " cannot be null.");
		initService();
		log.info("init");
	}

	@Override
	public void destroy()
	{

	}

	@Override
	public final void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException
	{
		final HttpServletRequest req = (HttpServletRequest) request;
		final HttpServletResponse res = (HttpServletResponse) response;

		if (CommonUtils.isNotEmpty(getServerLoginUrl()))
		{
			if (!UrlUtils.buildUrl(req).equals(this.getServerLoginUrl()))
			{
				filterChain.doFilter(request, response);
				return;
			}
		}
		String url = UrlUtils.buildUrl(req);
		req.setAttribute("serverLoginUrl", url);
		req.setAttribute("serviceParameterName", getServiceParameterName());
		String method = req.getHeader(TicketValidator.Method);

		if (method == null || method.trim().length() == 0)
		{
			method = req.getParameter(TicketValidator.Method);
		}
		if (TicketValidator.Logout.equalsIgnoreCase(method))
		{
			doLogout(req, res);
		}
		else if (TicketValidator.Validate.equalsIgnoreCase(method))
		{
			doValidate(req, res);
		}
		else if (TicketValidator.Login.equalsIgnoreCase(method))
		{
			doLogin(req, res);
		}
		else
		{
			Ticket ticket = TicketMap.instance.get(req.getSession().getId());
			String serviceUrl = getServiceUrl(req);
			if (ticket == null)
			{
				req.setAttribute(getServiceParameterName(), serviceUrl);
				req.getRequestDispatcher(loginPath).forward(request, response);
			}
			else
			{

				String redirectUrl = serviceUrl;
				redirectUrl = redirectUrl + (redirectUrl.indexOf("?") >= 0 ? "&" : "?") + this.getTicketParameterName() + "=" + ticket.getId();
				success(req, res, redirectUrl);
			}
		}
	}

	protected void doLogin(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
	{
		boolean noScript = CommonUtils.parseBoolean(req.getParameter("noScript"));
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		boolean passwordencoding = CommonUtils.parseBoolean(req.getParameter("passwordencoding"));
		TicketImpl ticket = new TicketImpl(req.getSession().getId());
		String serviceUrl = getServiceUrl(req);
		if (login(username, password, passwordencoding))
		{
			ticket.setSuccess(true);
			ticket.setUser(username);
			ticket.setServiceUrl(serviceUrl);
			ticket.setRedirect(serviceUrl + (serviceUrl.indexOf("?") >= 0 ? "&" : "?") + this.getTicketParameterName() + "=" + ticket.getId());
			req.getSession().setAttribute(ticket.getId(), ticket);
			TicketMap.instance.put(ticket.getId(), ticket);
			success(req, res, ticket.getRedirect());
		}
		else
		{
			if (!noScript)
			{
				errorMsg = "请开启浏览器的Javascript功能";
			}
			req.setAttribute(getServiceParameterName(), serviceUrl);
			ticket.setSuccess(false);
			req.setAttribute("errorMsg", errorMsg);
			req.getRequestDispatcher(loginPath).forward(req, res);
		}
		// CommonUtils.toJSON(req, res, ticket, Ticket.DateFromat);
	}

	private void success(HttpServletRequest req, HttpServletResponse res, String redirectUrl) throws IOException, ServletException
	{
		req.setAttribute(getServiceParameterName(), redirectUrl);
		if (CommonUtils.isEmpty(successPath))
		{
			res.sendRedirect(redirectUrl);
		}
		else
		{
			req.getRequestDispatcher(successPath).forward(req, res);
		}
	}

	private String getServiceUrl(HttpServletRequest req)
	{
		String serviceUrl = req.getParameter(getServiceParameterName());
		if (CommonUtils.isEmpty(serviceUrl))
		{
			serviceUrl = req.getContextPath();
		}
		return serviceUrl;
	}

	private boolean login(String username, String password, boolean passwordencoding)
	{
		try
		{
			if (!passwordencoding)
			{
				password = passwordEncoder.encode(password);
			}
			return ssoService.login(username, password, loginSQL);
		}
		catch (Exception ex)
		{
			errorMsg = ex.getMessage();
			log.error("", ex);
		}
		return false;
	}

	protected void doLogout(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		req.getSession().removeAttribute(req.getSession().getId());
		TicketMap.instance.remove(req.getSession().getId());
		String serviceUrl = getServiceUrl(req);
		req.setAttribute(getServiceParameterName(), serviceUrl);
		req.setAttribute("errorMsg", "注消成功！");
		req.getRequestDispatcher(logoutPath).forward(req, res);
	}

	protected void doValidate(HttpServletRequest req, HttpServletResponse res)
	{
		String ticketName = req.getParameter(this.getTicketParameterName());
		if (TicketMap.instance.containsKey(ticketName))
		{
			Ticket ticket = TicketMap.instance.get(ticketName);
			CommonUtils.toJSON(req, res, ticket, Ticket.DateFromat);
		}
	}

	public String getLoginSQL()
	{
		return loginSQL;
	}

	public void setLoginSQL(String loginSQL)
	{
		this.loginSQL = loginSQL;
	}

	public String getEncodingAlgorithm()
	{
		return encodingAlgorithm;
	}

	public void setEncodingAlgorithm(String encodingAlgorithm)
	{
		this.encodingAlgorithm = encodingAlgorithm;
	}

}
