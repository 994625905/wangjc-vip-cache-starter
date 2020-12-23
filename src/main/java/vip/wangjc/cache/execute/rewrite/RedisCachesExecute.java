package vip.wangjc.cache.execute.rewrite;

import vip.wangjc.cache.client.redis.CacheRedisTemplate;
import vip.wangjc.cache.execute.AbstractCachesExecute;

import java.util.concurrent.TimeUnit;

/**
 * redis缓存器
 * @author wangjc
 * @title: RedisLimitExecuteServiceImpl
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 17:18
 */
public class RedisCachesExecute extends AbstractCachesExecute {

    private final CacheRedisTemplate<String,Object> cacheRedisTemplate;

    public RedisCachesExecute(CacheRedisTemplate cacheRedisTemplate){
        this.cacheRedisTemplate = cacheRedisTemplate;
    }

    @Override
    public Object check(String key, long expire) {
        Object value = this.cacheRedisTemplate.opsForValue().get(this.key(key));

        if(value != null){
            this.cacheRedisTemplate.expire(key, expire, TimeUnit.SECONDS);// 刷新缓存
        }
        return value;
    }

    @Override
    public void set(Object value, String key, long expire) {
        this.cacheRedisTemplate.opsForValue().set(this.key(key),value,expire, TimeUnit.SECONDS);
    }
}
