package com.uijin.stockmanager.redis.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisClusterConfiguration clusterConfiguration) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory(clusterConfiguration));
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());
    return redisTemplate;
  }

  @Bean
  public LettuceConnectionFactory redisConnectionFactory(RedisClusterConfiguration clusterConfiguration) {
    return new LettuceConnectionFactory(clusterConfiguration);
  }

  @Bean
  public RedisClusterConfiguration clusterConfiguration() {
    RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();
    clusterConfig.setClusterNodes(Arrays.asList(
        new RedisNode("172.28.0.2", 6379),
        new RedisNode("172.28.0.3", 6380),
        new RedisNode("172.28.0.4", 6381),
        new RedisNode("172.28.0.5", 6382),
        new RedisNode("172.28.0.6", 6383),
        new RedisNode("172.28.0.7", 6384)
    ));
    return clusterConfig;
  }
}
