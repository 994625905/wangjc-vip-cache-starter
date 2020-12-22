package vip.wangjc.cache.builder;

import vip.wangjc.cache.annotation.Limiter;
import vip.wangjc.cache.builder.rewrite.IPLimitKeyBuilder;
import vip.wangjc.cache.builder.rewrite.NormalLimitKeyBuilder;
import vip.wangjc.cache.builder.rewrite.ParamLimitKeyBuilder;

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
