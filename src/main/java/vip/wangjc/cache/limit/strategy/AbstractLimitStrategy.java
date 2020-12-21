package vip.wangjc.cache.limit.strategy;

import vip.wangjc.cache.limit.entity.LimitEntity;

/**
 * 限流成功的执行策略
 * @author wangjc
 * @title: AbstractLimitStrategy
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 17:57
 */
public abstract class AbstractLimitStrategy {

    /**
     * 限流后的执行策略
     * @param limitEntity
     */
    public abstract Object strategy(LimitEntity limitEntity);

}
