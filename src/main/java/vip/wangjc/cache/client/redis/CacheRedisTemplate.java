package vip.wangjc.cache.client.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 构造专属的RedisTemplate模板
 * @author wangjc
 * @title: CacheRedisTemplate
 * @projectName wangjc-vip
 * @date 2020/12/23 - 14:48
 */
public class CacheRedisTemplate<K,V> extends RedisTemplate<K,V> {

    /**
     * 注入bean的构造器，改写序列化
     */
    public CacheRedisTemplate(){

        /** key和 hash key 均采用string方式序列化 */
        this.setKeySerializer(RedisSerializer.string());
        this.setHashKeySerializer(RedisSerializer.string());

        /** Jackson序列化 */
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(mapper);

        /** value和 hash value 均采用Jackson的方式序列化 */
        this.setValueSerializer(jackson2JsonRedisSerializer);
        this.setHashValueSerializer(jackson2JsonRedisSerializer);
    }

    /**
     * 声明bean的构造器
     * @param connectionFactory
     */
    public CacheRedisTemplate(RedisConnectionFactory connectionFactory){
        this();
        this.setConnectionFactory(connectionFactory);
        this.afterPropertiesSet();
    }

    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
        return new DefaultStringRedisConnection(connection);
    }
}
