package cn.gotom.sso.server;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.gotom.sso.Ticket;
import cn.gotom.sso.TicketImpl;
import cn.gotom.sso.TicketMap;
import cn.gotom.sso.TicketValidator;
import cn.gotom.sso.filter.AbstractCommonFilter;
import cn.gotom.sso.util.CommonUtils;
import cn.gotom.sso.util.GsonUtils;
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
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException
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
		else if (TicketValidator.Code.equalsIgnoreCase(method))
		{
			doCode(req, res);
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
		String code = req.getParameter("code");
		String codeVal = (String) req.getSession().getAttribute(TicketValidator.Code);
		boolean passwordencoding = CommonUtils.parseBoolean(req.getParameter("passwordencoding"));
		TicketImpl ticket = new TicketImpl(req.getSession().getId());
		String serviceUrl = getServiceUrl(req);
		Integer errorNum = (Integer) req.getSession().getAttribute(TicketValidator.Login);
		if (errorNum == null)
		{
			errorNum = 0;
		}
		if (errorNum > 3)
		{
			if (code!= null && code.equalsIgnoreCase(codeVal))
			{
				errorNum = 0;
			}
		}
		if (errorNum <= 3 && login(username, password, passwordencoding))
		{
			ticket.setSuccess(true);
			ticket.setUser(username);
			ticket.setServiceUrl(serviceUrl);
			ticket.setRedirect(serviceUrl + (serviceUrl.indexOf("?") >= 0 ? "&" : "?") + this.getTicketParameterName() + "=" + ticket.getId());
			req.getSession().setAttribute(ticket.getId(), ticket);
			TicketMap.instance.put(ticket.getId(), ticket);
			req.getSession().removeAttribute(TicketValidator.Login);
			req.getSession().removeAttribute(TicketValidator.Code);
			success(req, res, ticket.getRedirect());
		}
		else
		{
			if (errorNum > 3)
			{
				errorMsg = "验证码不正确！";
			}
			else
			{
				errorMsg = "用户名或密码不正确！";
			}
			req.getSession().setAttribute(TicketValidator.Login, ++errorNum);
			if (!noScript)
			{
				// errorMsg = "请开启浏览器的Javascript功能";
			}
			req.setAttribute(getServiceParameterName(), serviceUrl);
			ticket.setSuccess(false);
			req.setAttribute("errorMsg", errorMsg);
			req.getRequestDispatcher(loginPath).forward(req, res);
		}
		// CommonUtils.toJSON(req, res, ticket, Ticket.DateFromat);
	}

	protected void doCode(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		HttpSession session = request.getSession();
		response.setContentType("image/jpeg");
		OutputStream os = null;
		try
		{
			/** 生成随机码 */
			String code = "";
			for (int i = 0; i < 5; ++i)
			{
				Random seedRan = new Random();
				int seed = seedRan.nextInt();
				Random ran = new Random(seed);
				int num = Math.abs((ran.nextInt()) % 91);
				if (65 <= num && num <= 90)
				{
					char c = (char) num;
					code += c;
				}
				else
				{
					num = num % 10;
					code += num;
				}
			}
			/** 将验证码保存到会话中 */
			session.setAttribute(TicketValidator.Code, code);
			/** 生成验证图 */
			BufferedImage image = createImage(80, 30, code);
			/** 获取输出流 */
			os = response.getOutputStream();
			/** 将验证图写入输出流 */
			ImageIO.write(image, "JPEG", os);
			/** 刷新关闭 */
			os.flush();
			os.close();
		}
		catch (Exception ex)
		{
			log.error(ex.getClass() + " " + ex.getMessage());
		}
		finally
		{
			if (os != null)
			{
				try
				{
					os.close();
				}
				catch (IOException e)
				{
					log.error(e.getClass() + " " + e.getMessage());
				}
			}
		}
	}

	/**
	 * 根据给定尺寸及验证码生成验证图
	 * 
	 * @param width
	 * @param height
	 * @param code
	 * @return
	 */
	private BufferedImage createImage(int width, int height, String code)
	{
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Random random = new Random();
		/** 获取图片上下文 */
		Graphics graph = image.getGraphics();
		/** 设置背景颜色 */
		graph.setColor(createColor(200, 250));
		/** 填充图片区域 */
		graph.fillRect(0, 0, width, height);
		/** 随机产生10条干扰线 */
		/** 设置干扰线颜色 */
		graph.setColor(createColor(160, 200));
		/** 设置合适的字体 */
		int size = (int) (height * (2.0 / 3));
		Font font = new Font("黑体", Font.PLAIN, size);
		for (int i = 0; i < 20; i++)
		{
			int x1 = random.nextInt(width);
			int y1 = random.nextInt(height);
			int x2 = random.nextInt(width / 2);
			int y2 = random.nextInt(height / 2);
			graph.drawLine(x1, y1, x1 + x2, y1 + y2);
		}
		/** 将随机码一个个的写到图片上 */
		/** 根据图片高(宽)度以及验证码位数计算出随机码书写的XY坐标 */
		int length = code.length();
		int w = (int) ((width * 1.0) / (length + 1));
		int h = (int) (height * (3.0 / 4));
		graph.setFont(font);
		String[] array = code.split("");
		for (int i = 1; i < array.length; ++i)
		{
			graph.setColor(createColor(45, 60));
			String str = array[i];
			graph.drawString(str, w * i, h);
		}
		/** 释放图像上下文以及占用所有系统资源 */
		graph.dispose();
		return image;
	}

	/**
	 * 生成给定范围内随机颜色
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	private Color createColor(int begin, int end)
	{
		Random random = new Random();
		if (begin > 255 || begin < 0)
			begin = 255;
		if (end > 255 || end < 0)
			end = 255;
		int r = begin + random.nextInt(end - begin);
		int g = begin + random.nextInt(end - begin);
		int b = begin + random.nextInt(end - begin);
		return new Color(r, g, b);
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
			// CommonUtils.toJSON(req, res, ticket, Ticket.DateFromat);
			GsonUtils.writer(req, res, GsonUtils.toJson(ticket, Ticket.DateFromat));
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
