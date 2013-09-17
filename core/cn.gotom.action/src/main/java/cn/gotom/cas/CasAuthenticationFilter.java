package cn.gotom.cas;

import org.jasig.cas.client.authentication.AuthenticationFilter;

import cn.gotom.pojos.ResourceConfig;
import cn.gotom.pojos.ResourceName;
import cn.gotom.service.ResourceConfigService;

import com.google.inject.Inject;

public class CasAuthenticationFilter extends AuthenticationFilter
{
	@Inject
	protected ResourceConfigService resourceConfigService;

	public void init()
	{
		super.init();
		ResourceConfig casServerLoginUrl = resourceConfigService.getByName(ResourceName.casServerLoginUrl);
		ResourceConfig serverNameConf = resourceConfigService.getByName(ResourceName.casServerName);
		String serverName = serverNameConf.getResValue();
		if (serverName != null && serverName.endsWith("/"))
		{
			serverName = serverName.substring(0, serverName.length() - 1);
		}
		this.setCasServerLoginUrl(serverName + casServerLoginUrl.getResValue());
		this.setServerName(serverName);
	}
}
