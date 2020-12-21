package vip.wangjc.cache.limit.execute;

import org.springframework.data.redis.core.RedisTemplate;
import vip.wangjc.cache.limit.annotation.Limiter;
import vip.wangjc.cache.limit.execute.rewrite.RedisLimitExecute;

/**
 * 限流执行器的生成工厂
 * @author wangjc
 * @title: LimitExecurteFactory
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 18:18
 */
public class LimitExecuteFactory {

    private final RedisTemplate redisTemplate;

    public LimitExecuteFactory(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    /**
     * 构建限流执行器
     * @param limiter
     * @return
     */
    public AbstractLimitExecute buildExecute(Limiter limiter){
        switch (limiter.clientType()){
            case REDIS_TEMPLATE:
                return new RedisLimitExecute(this.redisTemplate);
            default:
                throw new IllegalArgumentException("error limit client argument");
        }
    }
}
