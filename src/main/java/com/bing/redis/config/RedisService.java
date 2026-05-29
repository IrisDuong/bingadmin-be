package com.bing.redis.config;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {
	private final RedisTemplate<String, Object> redisTemplate;
	private ValueOperations<String, Object> valueOperations;
	private HashOperations<String, String, Object> hashOperations;
	private SetOperations<String, Object> setOperations;
	
	@PostConstruct
	public void init() {
		this.valueOperations = redisTemplate.opsForValue();
		this.hashOperations = redisTemplate.opsForHash();
		this.setOperations = redisTemplate.opsForSet();
	}

	
	/**
	 * Single value 
	 */
	public void set(String key, Object value, long ttl) {
		valueOperations.set(key, value, Duration.ofMillis(ttl));
	}

	public void set(String key, Object[] value, long ttl) {
		setOperations.add(key, value);
		redisTemplate.expire(key, Duration.ofMillis(ttl));
	}
	
	public long sizeOfKey(String key) {
		return setOperations.size(key);
	}
	
	public boolean isMemberOfKey(String key, Object value) {
		return setOperations.isMember(key, value);
	}
	
	public Object get(String key) {
		return valueOperations.get(key);
	}
	
	public void delete(String key) {
		redisTemplate.delete(key);
	}
	
	public long getExpire(String key) {
		return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
	}
	/**
//	 * Hash 
//	 */
//	public void set(String key, String hashKey, Object Value) {
//		hashOperations.put(key, hashKey, Value);
//	}
//	
//	public Object get(String key, String hashKey) {
//		return hashOperations.get(key, hashKey);
//	}
//
//	public Map<String, Object> getAllHashEntries(String key) {
//		return hashOperations.entries(key);
//	}
//	
//	public Set<String> getAllHashKeys(String key){
//		return hashOperations.keys(key);
//	}
//	
//	public boolean existsByHashKeyAndValue(String key,String hashKey,Object incommingValue) {
//		return incommingValue.equals(hashOperations.get(key, hashKey));
//	}
//	
//	
//	public void deleteByHashKeys(String key, String[] hashKeys) {
//		hashOperations.delete(key, hashKeys);
//	}
//	
//	public void setExpireByHashKey(String key, Collection<String> hashKeys, long ttl) {
//		hashOperations.expire(key, Duration.ofMillis(ttl), hashKeys);
//	}
}
