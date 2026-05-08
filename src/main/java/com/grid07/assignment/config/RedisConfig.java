package com.grid07.assignment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;



@Configuration
public class RedisConfig {
    
   
   @Bean
   public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
      
    //Springboot now knows-how to communicate with redis server and perform operations on it using StringRedisTemplate
    return new StringRedisTemplate(redisConnectionFactory);
   }

}
