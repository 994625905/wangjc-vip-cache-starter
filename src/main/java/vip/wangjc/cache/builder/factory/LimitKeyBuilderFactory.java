package vip.wangjc.cache.builder.factory;

import vip.wangjc.cache.annotation.Limiter;
import vip.wangjc.cache.builder.IPLimitKeyBuilder;
import vip.wangjc.cache.builder.NormalLimitKeyBuilder;
import vip.wangjc.cache.builder.ParamLimitKeyBuilder;
import vip.wangjc.cache.builder.abstracts.AbstractLimitKeyBuilder;

/**
 * 限流key构建器的生成工厂
 * @author wangjc
 * @title: LimitKeyBuilderFactory
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/22 - 16:50
 */
public class LimitKeyBuilderFactory {

    /**
     * 生成
     * @param limiter
     * @return
     */
    public AbstractLimitKeyBuilder builder(Limiter limiter){
        switch (limiter.type()){
            case NORMAL:
                return new NormalLimitKeyBuilder();
            case IP:
                return new IPLimitKeyBuilder();
            case PARAM:
                return new ParamLimitKeyBuilder();
            default:
                throw new IllegalArgumentException("error limit type argument");
        }
    }

}
