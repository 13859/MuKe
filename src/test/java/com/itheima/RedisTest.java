package com.itheima;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void testRedis() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("username","zhangsan");
        valueOperations.set("id","1",15, TimeUnit.SECONDS);
    }
    @Test
    public void testGet() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String username = (String) valueOperations.get("username");
        System.out.println(username);
    }
}
