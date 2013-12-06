package cn.gotom.sso.authentication;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jasig.cas.client.authentication.DefaultGatewayResolverImpl;
import org.jasig.cas.client.authentication.GatewayResolver;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;

import cn.gotom.sso.util.PathMatcher;
import cn.gotom.sso.util.PathMatcherAnt;
import cn.gotom.sso.util.UrlUtils;

public class AuthenticationFilter extends AbstractCasFilter
{

	protected final Logger log = Logger.getLogger(getClass());

	/**
	 * The URL to the CAS Server login.
	 */
	private String casServerLoginUrl;

	/**
	 * Whether to send the renew request or not.
	 */
	private boolean renew = false;

	/**
	 * Whether to send the gateway request or not.
	 */
	private boolean gateway = false;

	private GatewayResolver gatewayStorage = new DefaultGatewayResolverImpl();

	private final PathMatcher urlMatcher = new PathMatcherAnt();

	private String[] authenticationNones;

	private void initAuthenticationNone(FilterConfig filterConfig)
	{
		String none = getPropertyFromInitParams(filterConfig, "authenticationNone", null);
		if (none != null)
		{
			none = none.trim().replace("；", ";");
			none = none.replace(",", ";");
			none = none.replace("，", ";");
			none = none.replace("\n", ";");
			authenticationNones = none.trim().split(";");
			for (int i = 0; i < authenticationNones.length; i++)
			{
				authenticationNones[i] = authenticationNones[i].trim();
			}
		}
	}

	private boolean authenticationNone(String url)
	{
		if (authenticationNones != null)
		{
			for (String pattern : authenticationNones)
			{
				if (urlMatcher.pathMatchesUrl(pattern.trim(), url))
				{
					return true;
				}
			}
		}
		return false;
	}

	protected void initInternal(final FilterConfig filterConfig) throws ServletException
	{
		initAuthenticationNone(filterConfig);
		if (!isIgnoreInitConfiguration())
		{
			super.initInternal(filterConfig);
			setCasServerLoginUrl(getPropertyFromInitParams(filterConfig, "casServerLoginUrl", null));
			log.trace("Loaded CasServerLoginUrl parameter: " + this.casServerLoginUrl);
			setRenew(parseBoolean(getPropertyFromInitParams(filterConfig, "renew", "false")));
			log.trace("Loaded renew parameter: " + this.renew);
			setGateway(parseBoolean(getPropertyFromInitParams(filterConfig, "gateway", "false")));
			log.trace("Loaded gateway parameter: " + this.gateway);

			final String gatewayStorageClass = getPropertyFromInitParams(filterConfig, "gatewayStorageClass", null);

			if (gatewayStorageClass != null)
			{
				try
				{
					this.gatewayStorage = (GatewayResolver) Class.forName(gatewayStorageClass).newInstance();
				}
				catch (final Exception e)
				{
					log.error(e, e);
					throw new ServletException(e);
				}
			}
		}
	}

	public void init()
	{
		super.init();
		CommonUtils.assertNotNull(this.casServerLoginUrl, "casServerLoginUrl cannot be null.");
	}

	public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException
	{
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;
		String url = UrlUtils.buildUrl(request);
		if (authenticationNone(url))
		{
			filterChain.doFilter(request, response);
			return;
		}
		//log.debug(url);
		final HttpSession session = request.getSession(false);
		final Assertion assertion = session != null ? (Assertion) session.getAttribute(CONST_CAS_ASSERTION) : null;

		if (assertion != null)
		{
			filterChain.doFilter(request, response);
			return;
		}

		final String serviceUrl = constructServiceUrl(request, response);
		final String ticket = CommonUtils.safeGetParameter(request, getArtifactParameterName());
		final boolean wasGatewayed = this.gatewayStorage.hasGatewayedAlready(request, serviceUrl);

		if (CommonUtils.isNotBlank(ticket) || wasGatewayed)
		{
			filterChain.doFilter(request, response);
			return;
		}

		final String modifiedServiceUrl;

		log.debug("no ticket and no assertion found");
		if (this.gateway)
		{
			log.debug("setting gateway attribute in session");
			modifiedServiceUrl = this.gatewayStorage.storeGatewayInformation(request, serviceUrl);
		}
		else
		{
			modifiedServiceUrl = serviceUrl;
		}

		if (log.isDebugEnabled())
		{
			log.debug("Constructed service url: " + modifiedServiceUrl);
		}

		final String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.casServerLoginUrl, getServiceParameterName(), modifiedServiceUrl, this.renew, this.gateway);

		if (log.isDebugEnabled())
		{
			log.debug("redirecting to \"" + urlToRedirectTo + "\"");
		}

		response.sendRedirect(urlToRedirectTo);
	}

	public final void setRenew(final boolean renew)
	{
		this.renew = renew;
	}

	public final void setGateway(final boolean gateway)
	{
		this.gateway = gateway;
	}

	public final void setCasServerLoginUrl(final String casServerLoginUrl)
	{
		this.casServerLoginUrl = casServerLoginUrl;
	}

	public final void setGatewayStorage(final GatewayResolver gatewayStorage)
	{
		this.gatewayStorage = gatewayStorage;
	}
}
