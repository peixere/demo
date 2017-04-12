package cc.cnplay.cloud.feign;

import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("cloud-service")
public interface ConsumerClient
{
	//@RequestMapping(value = "/")
	public String home(String username);

}