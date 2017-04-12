package cc.cnplay.cloud.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer // 注解启动一个服务注册中心提供给其他应用进行注册
@SpringBootApplication
public class CloudEurekaServerApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(CloudEurekaServerApplication.class, args);
	}
}
