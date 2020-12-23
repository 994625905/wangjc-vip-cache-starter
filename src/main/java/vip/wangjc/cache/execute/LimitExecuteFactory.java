package vip.wangjc.cache.execute;

import vip.wangjc.cache.annotation.Limiter;
import vip.wangjc.cache.client.redis.CacheRedisTemplate;
import vip.wangjc.cache.execute.rewrite.RedisLimitExecute;

/**
 * 限流执行器的生成工厂
 * @author wangjc
 * @title: LimitExecurteFactory
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 18:18
 */
public class LimitExecuteFactory {

    private final CacheRedisTemplate cacheRedisTemplate;

    public LimitExecuteFactory(CacheRedisTemplate cacheRedisTemplate){
        this.cacheRedisTemplate = cacheRedisTemplate;
    }

    /**
     * 构建限流执行器
     * @param limiter
     * @return
     */
    public AbstractLimitExecute buildExecute(Limiter limiter){
        switch (limiter.clientType()){
            case REDIS_TEMPLATE:
                return new RedisLimitExecute(this.cacheRedisTemplate);
            default:
                throw new IllegalArgumentException("error limit client argument");
        }
    }
}
