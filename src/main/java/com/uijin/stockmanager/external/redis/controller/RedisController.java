package com.uijin.stockmanager.external.redis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RedisController {

  private final RedisTemplate<String, Object> redisTemplate;

  @GetMapping("/test")
  public String testReds() {
    redisTemplate.opsForValue().set("testKey", "test");

    String value = (String) redisTemplate.opsForValue().get("testKey");
    return value;
  }
}
