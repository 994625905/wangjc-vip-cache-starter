package vip.wangjc.cache.execute;

import org.springframework.data.redis.core.RedisTemplate;
import vip.wangjc.cache.annotation.Caches;
import vip.wangjc.cache.annotation.Flush;
import vip.wangjc.cache.execute.rewrite.RedisCachesExecute;

/**
 * 缓存执行器的生成工厂
 * @author wangjc
 * @title: CachesExecuteFactory
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 18:18
 */
public class CachesExecuteFactory {

    private final RedisTemplate redisTemplate;

    public CachesExecuteFactory(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    /**
     * 构建缓存执行器
     * @param caches
     * @return
     */
    public AbstractCachesExecute buildExecute(Caches caches){
        switch (caches.clientType()){
            case REDIS_TEMPLATE:
                return new RedisCachesExecute(this.redisTemplate);
            default:
                throw new IllegalArgumentException("error caches client argument");
        }
    }

    /**
     * 构建缓存执行器
     * @param flush
     * @return
     */
    public AbstractCachesExecute buildExecute(Flush flush){
        switch (flush.clientType()){
            case REDIS_TEMPLATE:
                return new RedisCachesExecute(this.redisTemplate);
            default:
                throw new IllegalArgumentException("error caches client argument");
        }
    }

}
