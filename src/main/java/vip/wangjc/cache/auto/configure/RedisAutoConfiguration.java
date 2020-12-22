package vip.wangjc.cache.auto.configure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
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
     * @param factory
     * @return
     */
    @Bean
    @ConditionalOnClass({RedisOperations.class})
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(mapper);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用 String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的 key也采用 String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用 jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的 value序列化方式采用 jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }

    /**
     * 注入redisService
     * @param redisTemplate
     * @return
     */
    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public RedisService redisService(RedisTemplate redisTemplate){
        return new RedisServiceImpl(redisTemplate);
    }

}
