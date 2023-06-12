package com.zjz.template;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisTemplate {

	private StringRedisTemplate redisTemplate;

	public String getStringVaue(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	public void setStringValue(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	public void setStringValue(String key, String value, long timeout,
			TimeUnit unit) {
		redisTemplate.opsForValue().set(key, value, timeout, unit);
	}

	public <T> T getHash(String key, Object hashKey, Class<T> type) {
		Object result = redisTemplate.opsForHash().get(key, hashKey);
		return (T) result;
	}
}
