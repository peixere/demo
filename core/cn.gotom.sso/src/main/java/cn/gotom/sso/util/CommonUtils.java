package cn.gotom.sso.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * Common utilities so that we don't need to include log4j
 * 
 * @author peixere@qq.com
 * @version $Revision: 11729 $ $Date: 2007-09-26 14:22:30 -0400 (Tue, 26 Sep 2007) $
 * @since 3.0
 */
public final class CommonUtils
{

	private static final Logger log = Logger.getLogger(CommonUtils.class);

	private CommonUtils()
	{
		// nothing to do
	}

	public static String formatForUtcTime(final Date date)
	{
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(date);
	}

	public static void assertNotNull(final Object object, final String message)
	{
		if (object == null)
		{
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertNotEmpty(final Collection<?> c, final String message)
	{
		assertNotNull(c, message);
		if (c.isEmpty())
		{
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertTrue(final boolean cond, final String message)
	{
		if (!cond)
		{
			throw new IllegalArgumentException(message);
		}
	}

	public static boolean isEmpty(final String value)
	{
		return (value != null) ? (value.trim().length() == 0) : true;
	}

	public static boolean parseBoolean(final String value)
	{
		return ((value != null) && value.equalsIgnoreCase("true"));
	}

	public static int parseInt(final String value)
	{
		try
		{
			return Integer.parseInt(value);
		}
		catch (Exception ex)
		{
			return 0;
		}
	}

	public static boolean isNotEmpty(final String string)
	{
		return !isEmpty(string);
	}

	public static boolean isBlank(final String string)
	{
		return isEmpty(string) || string.trim().length() == 0;
	}

	public static boolean isNotBlank(final String string)
	{
		return !isBlank(string);
	}

	public static String constructRedirectUrl(final String serverLoginUrl, final String serviceParameter, final String serviceUrl)
	{
		try
		{
			return serverLoginUrl + (serverLoginUrl.indexOf("?") != -1 ? "&" : "?") + serviceParameter + "=" + URLEncoder.encode(serviceUrl, "UTF-8");
		}
		catch (final UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}
	}

	// public static void readAndRespondToProxyReceptorRequest(final HttpServletRequest request, final HttpServletResponse response, final ProxyGrantingTicketStorage proxyGrantingTicketStorage) throws IOException
	// {
	// final String proxyGrantingTicketIou = request.getParameter(PARAM_PROXY_GRANTING_TICKET_IOU);
	//
	// final String proxyGrantingTicket = request.getParameter(PARAM_PROXY_GRANTING_TICKET);
	//
	// if (CommonUtils.isBlank(proxyGrantingTicket) || CommonUtils.isBlank(proxyGrantingTicketIou))
	// {
	// response.getWriter().write("");
	// return;
	// }
	//
	// if (LOG.isDebugEnabled())
	// {
	// LOG.debug("Received proxyGrantingTicketId [" + proxyGrantingTicket + "] for proxyGrantingTicketIou [" + proxyGrantingTicketIou + "]");
	// }
	//
	// proxyGrantingTicketStorage.save(proxyGrantingTicketIou, proxyGrantingTicket);
	//
	// if (LOG.isDebugEnabled())
	// {
	// LOG.debug("Successfully saved proxyGrantingTicketId [" + proxyGrantingTicket + "] for proxyGrantingTicketIou [" + proxyGrantingTicketIou + "]");
	// }
	//
	// response.getWriter().write("<?xml version=\"1.0\"?>");
	// response.getWriter().write("<casClient:proxySuccess xmlns:casClient=\"http://www.yale.edu/tp/casClient\" />");
	// }

	public static String getServerName(HttpServletRequest r)
	{

		String scheme = r.getScheme().toLowerCase();
		String serverName = r.getServerName();
		int serverPort = r.getServerPort();
		StringBuilder url = new StringBuilder();
		url.append(scheme).append("://").append(serverName);
		if ("http".equals(scheme))
		{
			if (serverPort != 80)
			{
				url.append(":").append(serverPort);
			}
		}
		else if ("https".equals(scheme))
		{
			if (serverPort != 443)
			{
				url.append(":").append(serverPort);
			}
		}
		return url.toString();
	}

	public static String constructServiceUrl(final HttpServletRequest request, final HttpServletResponse response, final String service, final String serverName, final String ticketParameterName, final boolean encode)
	{
		if (CommonUtils.isNotBlank(service))
		{
			return encode ? response.encodeURL(service) : service;
		}
		final StringBuilder buffer = new StringBuilder();
		if (CommonUtils.isNotBlank(serverName))
		{
			if (!serverName.startsWith("https://") && !serverName.startsWith("http://"))
			{
				buffer.append(request.isSecure() ? "https://" : "http://");
			}

			buffer.append(serverName);
		}
		else
		{
			buffer.append(getServerName(request));
		}
		buffer.append(request.getRequestURI());

		if (CommonUtils.isNotBlank(request.getQueryString()))
		{
			final int location = request.getQueryString().indexOf(ticketParameterName + "=");

			if (location == 0)
			{
				final String returnValue = encode ? response.encodeURL(buffer.toString()) : buffer.toString();
				return returnValue;
			}

			buffer.append("?");

			if (location == -1)
			{
				buffer.append(request.getQueryString());
			}
			else if (location > 0)
			{
				final int actualLocation = request.getQueryString().indexOf("&" + ticketParameterName + "=");

				if (actualLocation == -1)
				{
					buffer.append(request.getQueryString());
				}
				else if (actualLocation > 0)
				{
					buffer.append(request.getQueryString().substring(0, actualLocation));
				}
			}
		}

		final String returnValue = encode ? response.encodeURL(buffer.toString()) : buffer.toString();
		return returnValue;
	}

	public static String safeGetParameter(final HttpServletRequest request, final String parameter)
	{
		if ("POST".equals(request.getMethod()) && "logoutRequest".equals(parameter))
		{
			log.debug("safeGetParameter called on a POST HttpServletRequest for LogoutRequest.  Cannot complete check safely.  Reverting to standard behavior for this Parameter");
			return request.getParameter(parameter);
		}
		return request.getQueryString() == null || request.getQueryString().indexOf(parameter) == -1 ? null : request.getParameter(parameter);
	}

	public static String getResponseFromServer(final URL constructedUrl, final String encoding, final String sessionId)
	{
		return getResponseFromServer(constructedUrl, HttpsURLConnection.getDefaultHostnameVerifier(), encoding, sessionId);
	}

	public static String getResponseFromServer(final URL constructedUrl, final HostnameVerifier hostnameVerifier, final String encoding, final String sessionId)
	{
		URLConnection conn = null;
		try
		{
			conn = constructedUrl.openConnection();
			if (conn instanceof HttpsURLConnection)
			{
				((HttpsURLConnection) conn).setHostnameVerifier(hostnameVerifier);
				((HttpsURLConnection) conn).setRequestProperty("Cookie", sessionId);
			}
			final BufferedReader in;

			if (CommonUtils.isEmpty(encoding))
			{
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			}
			else
			{
				in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
			}

			String line;
			final StringBuilder stringBuffer = new StringBuilder(255);

			while ((line = in.readLine()) != null)
			{
				stringBuffer.append(line);
				stringBuffer.append("\n");
			}
			return stringBuffer.toString();
		}
		catch (final Exception e)
		{
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		finally
		{
			if (conn != null && conn instanceof HttpURLConnection)
			{
				((HttpURLConnection) conn).disconnect();
			}
		}

	}

	public static String getResponseFromServer(final String url, String encoding, final String sessionId)
	{
		try
		{
			return getResponseFromServer(new URL(url), encoding, sessionId);
		}
		catch (final MalformedURLException e)
		{
			throw new IllegalArgumentException(e);
		}
	}

	// public static ProxyList createProxyList(final String proxies)
	// {
	// if (CommonUtils.isBlank(proxies))
	// {
	// return new ProxyList();
	// }
	//
	// final ProxyListEditor editor = new ProxyListEditor();
	// editor.setAsText(proxies);
	// return (ProxyList) editor.getValue();
	// }

	public static void sendRedirect(final HttpServletResponse response, final String url)
	{
		try
		{
			response.sendRedirect(url);
		}
		catch (final Exception e)
		{
			log.warn(e.getMessage(), e);
		}

	}

	public static String getServerUrlPrefix(HttpServletRequest r, String casServerUrlPrefix)
	{
		if (!casServerUrlPrefix.startsWith("https://") && !casServerUrlPrefix.startsWith("http://"))
		{
			casServerUrlPrefix = getServerName(r) + casServerUrlPrefix;
		}
		return casServerUrlPrefix;
	}

	public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens)
	{

		if (str == null)
		{
			return null;
		}
		StringTokenizer st = new StringTokenizer(str, delimiters);
		List<String> tokens = new ArrayList<String>();
		while (st.hasMoreTokens())
		{
			String token = st.nextToken();
			if (trimTokens)
			{
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0)
			{
				tokens.add(token);
			}
		}
		return toStringArray(tokens);
	}

	public static String[] toStringArray(Collection<String> collection)
	{
		if (collection == null)
		{
			return null;
		}
		return collection.toArray(new String[collection.size()]);
	}

	public static boolean hasLength(CharSequence str)
	{
		return (str != null && str.length() > 0);
	}

	public static int countOccurrencesOf(String str, String sub)
	{
		if (str == null || sub == null || str.length() == 0 || sub.length() == 0)
		{
			return 0;
		}
		int count = 0;
		int pos = 0;
		int idx;
		while ((idx = str.indexOf(sub, pos)) != -1)
		{
			++count;
			pos = idx + sub.length();
		}
		return count;
	}

	public static boolean hasText(CharSequence str)
	{
		if (!hasLength(str))
		{
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++)
		{
			if (!Character.isWhitespace(str.charAt(i)))
			{
				return true;
			}
		}
		return false;
	}

	public static void printParameters(HttpServletRequest request)
	{
		Map<String, String[]> params = request.getParameterMap();
		StringBuffer sb = new StringBuffer();
		for (String key : params.keySet())
		{
			sb.append("\n" + key + "=");
			String[] values = params.get(key);
			for (String value : values)
			{
				sb.append(value + ",");
			}
		}
		log.debug(sb);
	}

	//
	// class JsonDateValueProcessor implements JsonValueProcessor
	// {
	//
	// private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	//
	// public JsonDateValueProcessor()
	// {
	// }
	//
	// public JsonDateValueProcessor(String dateFormat)
	// {
	// this.dateFormat = new SimpleDateFormat(dateFormat);
	// }
	//
	// @Override
	// public Object processArrayValue(Object value, JsonConfig jsonConfig)
	// {
	// return null;
	// }
	//
	// @Override
	// public Object processObjectValue(String key, Object value, JsonConfig jsonConfig)
	// {
	// if (value == null)
	// {
	// return "";
	// }
	// if (value instanceof java.sql.Timestamp || value instanceof java.util.Date)
	// {
	// String str = dateFormat.format((java.util.Date) value);
	// return str;
	// }
	// return value;
	// }
	//
	// }
	//
	// public static void toJSON(HttpServletRequest request, HttpServletResponse response, Object value, String dateFormat, String[] excludes)
	// {
	// JsonConfig config = new JsonConfig();
	// if (excludes != null && excludes.length > 0)
	// {
	// config.setExcludes(excludes);
	// }
	// // config.setIgnoreDefaultExcludes(false);
	// config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
	// if (dateFormat != null && dateFormat.trim().length() > 0)
	// {
	// JsonValueProcessor dateValueProcessor = new JsonDateValueProcessor();
	// config.registerJsonValueProcessor(java.sql.Timestamp.class, dateValueProcessor);
	// config.registerJsonValueProcessor(java.util.Date.class, dateValueProcessor);
	// }
	// JSON json = net.sf.json.JSONSerializer.toJSON(value, config);
	// String encoing = request.getCharacterEncoding();
	// if (CommonUtils.isEmpty(encoing))
	// {
	// encoing = "utf-8";
	// }
	// // response.setContentType("text/html;charset=" + encoing);
	// response.setContentType("application/json;charset=" + encoing);
	// try
	// {
	// response.getWriter().println(json.toString());
	// }
	// catch (IOException e)
	// {
	// e.printStackTrace();
	// log.error("输出JSON异常 " + value);
	// }
	// finally
	// {
	// try
	// {
	// response.getWriter().flush();
	// response.getWriter().close();
	// }
	// catch (IOException e)
	// {
	// log.error("输出JSON异常 " + value);
	// }
	// }
	// }
	//
	// public static void toJSON(HttpServletRequest request, HttpServletResponse response, Object value)
	// {
	// toJSON(request, response, value, null);
	// }
	//
	// public static void toJSON(HttpServletRequest request, HttpServletResponse response, Object value, String dateFormat)
	// {
	// toJSON(request, response, value, dateFormat, null);
	// }

	public static String formatXML(String inputXML)
	{
		SAXReader reader = new SAXReader();
		XMLWriter writer = null;
		try
		{
			Document document = (Document) reader.read(new StringReader(inputXML));
			StringWriter stringWriter = new StringWriter();
			OutputFormat format = new OutputFormat(" ", true);
			writer = new XMLWriter(stringWriter, format);
			writer.write(document);
			writer.flush();
			inputXML = stringWriter.getBuffer().toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage());
		}
		finally
		{
			if (writer != null)
			{
				try
				{
					writer.close();
				}
				catch (IOException e)
				{
				}
			}
		}
		return inputXML;
	}
}
