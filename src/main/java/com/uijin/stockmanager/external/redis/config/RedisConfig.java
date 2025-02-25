package com.uijin.stockmanager.external.redis.config;

import java.util.Arrays;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisClusterConfiguration clusterConfiguration) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory(clusterConfiguration));
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
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
        new RedisNode("127.0.0.1", 7001),
        new RedisNode("127.0.0.1", 7002),
        new RedisNode("127.0.0.1", 7003),
        new RedisNode("127.0.0.1", 7004),
        new RedisNode("127.0.0.1", 7005),
        new RedisNode("127.0.0.1", 7006)
    ));
    return clusterConfig;
  }

  @Bean
  public RedissonClient redissonClient() {
    Config config = new Config();
    config.useClusterServers()
        .addNodeAddress(
            "redis://127.0.0.1:7001",
            "redis://127.0.0.1:7002",
            "redis://127.0.0.1:7003",
            "redis://127.0.0.1:7004",
            "redis://127.0.0.1:7005",
            "redis://127.0.0.1:7006"
        )
        .setMasterConnectionPoolSize(64)
        .setSlaveConnectionPoolSize(64);
    return Redisson.create(config);
  }
}
