package cc.cnplay.cloud.feign;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController
{

	@Resource
	ConsumerClient client;

	@RequestMapping(value = "/")
	public String home(String username)
	{
		return client.home(username);
	}

}