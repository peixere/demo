package cc.cnplay.cloud.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
@EnableDiscoveryClient
@SpringBootApplication
public class CloudServiceApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(CloudServiceApplication.class, args);
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.redis")
	JedisConnectionFactory jedisConnectionFactory()
	{
		return new JedisConnectionFactory();
	}
}
