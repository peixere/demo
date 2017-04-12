package cc.cnplay.cloud.service.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CloudServiceController
{

	private final Logger logger = Logger.getLogger(getClass());

	@Resource
	private DiscoveryClient client;

	@RequestMapping(value = "/")
	public String home(String username, HttpSession httpSession)
	{
		ServiceInstance instance = client.getLocalServiceInstance();
		String str = instance.getServiceId() + "@" + instance.getHost() + instance.getPort() + "/#sid=" + httpSession.getId() + ", hello:" + username;
		logger.debug(str);
		return str;
	}

}