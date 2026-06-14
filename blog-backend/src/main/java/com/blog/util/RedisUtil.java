package com.blog.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // 存值
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    // 存值+过期时间
    public void set(String key, String value, long time, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, value, time, unit);
    }

    // 取值
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    // 自增（浏览量）
    public long increment(String key) {
        return stringRedisTemplate.opsForValue().increment(key);
    }

    // 删除缓存
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }
}
