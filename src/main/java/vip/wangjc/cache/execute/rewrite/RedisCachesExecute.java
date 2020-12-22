package vip.wangjc.cache.execute.rewrite;

import org.springframework.data.redis.core.RedisTemplate;
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

    private final RedisTemplate<String,Object> redisTemplate;

    public RedisCachesExecute(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Object check(String key, long expire) {
        Object value = this.redisTemplate.opsForValue().get(this.key(key));

        if(value != null){
            this.redisTemplate.expire(key, expire, TimeUnit.SECONDS);// 刷新缓存
        }
        return value;
    }

    @Override
    public void set(Object value, String key, long expire) {
        this.redisTemplate.opsForValue().set(this.key(key),value,expire, TimeUnit.SECONDS);
    }
}
