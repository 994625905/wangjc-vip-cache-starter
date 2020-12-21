package vip.wangjc.cache.limit.strategy.rewrite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.wangjc.cache.limit.entity.LimitEntity;
import vip.wangjc.cache.limit.strategy.AbstractLimitStrategy;

/**
 * 默认的策略
 * @author wangjc
 * @title: DefaultLimitStrategy
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 18:13
 */
public class DefaultLimitStrategy extends AbstractLimitStrategy {

    private static final Logger logger = LoggerFactory.getLogger(DefaultLimitStrategy.class);

    @Override
    public Object strategy(LimitEntity limitEntity) {
        logger.error("key[{}] has been limit,period[{}],count[{}]",limitEntity.getKey(),limitEntity.getPeriod(),limitEntity.getCount());
        return null;
    }
}
