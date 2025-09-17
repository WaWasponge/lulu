package org.lhh.spongehome.utils;

import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Data
public class RedisRateLimiter {

    private final RedisTemplate<String, String> redisTemplate;
    private final String keyPrefix;
    private final int capacity;
    private final int refillPeriod;
    private final TimeUnit timeUnit;

    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getRefillPeriod() {
        return refillPeriod;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public RedisRateLimiter(
            RedisTemplate<String, String> redisTemplate,
            String keyPrefix,
            int capacity,
            int refillPeriod,
            TimeUnit timeUnit) {
        this.redisTemplate = redisTemplate;
        this.keyPrefix = keyPrefix;
        this.capacity = capacity;
        this.refillPeriod = refillPeriod;
        this.timeUnit = timeUnit;
    }
    public boolean tryAcquire(String identifier) {
        String key = keyPrefix + identifier;
        long now  = System.currentTimeMillis();
        //使用lua脚本保证原子性操作
        String script =
                "local current = tonumber(redis.call('get', KEYS[1]) or 0)\n" +
                "if current > 0 then\n" +
                "    redis.call('decr', KEYS[1])\n" +
                "    return 1\n" +
                "else\n" +
                "    return 0\n" +
                "end";

        Long result = redisTemplate.execute(
    new DefaultRedisScript<>(script, Long.class),
                Collections.singletonList(key)
        );
        if(result != null && result == 1){
            return true;
        }

//        桶为空时，检查是否需要补充
        Long expire = redisTemplate.getExpire(key);
        if(expire == null || expire < 0){
//            重置桶并设置过期时间
            redisTemplate.opsForValue().set(key, String.valueOf( capacity - 1) );
            redisTemplate.expire(key, refillPeriod, timeUnit);
            return true;
        }
        return false;
    }
}
