package org.lhh.spongehome.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类，用于配置RedisTemplate
 */
@Configuration
public class redisConfig {

    /**
     * 配置RedisTemplate Bean
     * 
     * @param connectionFactory Redis连接工厂
     * @return 配置好的RedisTemplate实例
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
       // 创建RedisTemplate实例
       RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
       
       // 设置Redis连接工厂
       redisTemplate.setConnectionFactory(connectionFactory);
       
       // 初始化RedisTemplate的属性
       redisTemplate.afterPropertiesSet();
       
       // 启用Redis事务支持
       redisTemplate.setEnableTransactionSupport(true);
       //使用了 JSON 序列化器来对值进行序列化和反序列化
       redisTemplate.setKeySerializer(new StringRedisSerializer());

       redisTemplate.setValueSerializer(new StringRedisSerializer());
       // 返回配置好的RedisTemplate实例
       return redisTemplate;
    }
}