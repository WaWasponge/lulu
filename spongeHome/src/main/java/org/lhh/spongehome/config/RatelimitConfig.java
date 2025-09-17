package org.lhh.spongehome.config;


import org.lhh.spongehome.utils.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * time 2025/6/3 16:03
 * @author lhh
 * 限流配置类
 */
@Configuration
public class RatelimitConfig {

//    基于IP的登录限流 每分钟设置10次
    @Bean
    public RedisRateLimiter ipLoginRateLimiter(RedisTemplate<String , String> redisTemplate) {

        return new RedisRateLimiter(
                redisTemplate,
                "login:ip",
                10,
                1,
                TimeUnit.MINUTES);
    }

//    基于账号的登录限流，每小时五次
    @Bean
    public RedisRateLimiter accountLoginRateLimiter(RedisTemplate<String , String> redisTemplate) {

        return  new RedisRateLimiter(
                redisTemplate,
                "login:account",
                5,
                1,
                TimeUnit.HOURS);
    }

}
