package vip.wangjc.cache.auto.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import vip.wangjc.cache.client.redis.CacheRedisTemplate;
import vip.wangjc.cache.service.RedisService;
import vip.wangjc.cache.service.impl.RedisServiceImpl;

/**
 * 缓存组件的自动化配置
 * @author wangjc
 * @title: CacheAutoConfiguration
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/20 - 16:32
 */
@Configuration
@ConditionalOnClass({RedisOperations.class})
public class RedisAutoConfiguration {

    /**
     * 重新定义redisTemplate的bean，更改序列号的方式（默认使用JDK的序列化方式）
     * ConditionalOnClass：当给定的类名在类路径上存在，则实例化当前Bean
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    @ConditionalOnClass({RedisOperations.class})
    public CacheRedisTemplate<String,Object> cacheRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        return new CacheRedisTemplate<>(redisConnectionFactory);
    }

    /**
     * 注入redisService
     * @param cacheRedisTemplate
     * @return
     */
    @Bean
    @ConditionalOnBean(CacheRedisTemplate.class)
    public RedisService redisService(CacheRedisTemplate cacheRedisTemplate){
        return new RedisServiceImpl(cacheRedisTemplate);
    }

}
