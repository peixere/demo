package cc.cnplay.cloud.ribbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableRedisHttpSession
@EnableDiscoveryClient
@SpringBootApplication
public class RibbonApplication
{

	@Bean
	@LoadBalanced
	RestTemplate restTemplate()
	{
		return new RestTemplate();
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.redis")
	JedisConnectionFactory jedisConnectionFactory()
	{
		return new JedisConnectionFactory();
	}

	public static void main(String[] args)
	{
		SpringApplication.run(RibbonApplication.class, args);
	}

}
