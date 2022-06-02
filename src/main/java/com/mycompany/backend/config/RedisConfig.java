package com.mycompany.backend.config;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
public class RedisConfig {//쓰는 이유 : 토큰은 메모리에 저장하여 db 접근을 줄이기 위해 , 토큰 값을 해킹????
  @Value("${spring.redis.hostName}")
  private String hostName;
  @Value("${spring.redis.port}")
  private String port;
  @Value("${spring.redis.password}")
  private String password;
  
  //@Resource
  //RedisConnectionFactory redisConnectionFactory;
  
  //redis를 연결한다 
  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    log.info("실행");
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
    config.setHostName(hostName);
    config.setPort(Integer.parseInt(port));
    config.setPassword(password);
    LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(config);
    
    return connectionFactory;
  }
  
  //연결 정보를 갖고 가는 bean
  @Bean
  public RedisTemplate<String, String> redisTemplate(){
    log.info("실행");
    RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory());//연결 정보 
    redisTemplate.setKeySerializer(new StringRedisSerializer());//바이트 배열로 만들 때 사용하는 객체를 넣어라
    redisTemplate.setValueSerializer(new StringRedisSerializer());//
    return redisTemplate;
  }
 

}
