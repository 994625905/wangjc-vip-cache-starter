package vip.wangjc.cache.annotation;

import vip.wangjc.cache.builder.AbstractLimitKeyBuilder;
import vip.wangjc.cache.builder.rewrite.NormalLimitKeyBuilder;
import vip.wangjc.cache.entity.CacheClientType;
import vip.wangjc.cache.strategy.AbstractLimitStrategy;
import vip.wangjc.cache.strategy.rewrite.DefaultLimitStrategy;

import java.lang.annotation.*;

/**
 * 自定义注解，控制方法的限流器
 * @author wangjc
 * @title: Limiter
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 15:21
 */
@Target({ElementType.METHOD}) // 只作用于方法
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Limiter {

    /**
     * 限流的key生成器，默认是普通方法限流
     * @return
     */
    Class<? extends AbstractLimitKeyBuilder> keyBuilder() default NormalLimitKeyBuilder.class;

    /**
     * 限流成功后的策略，
     * @return
     */
    Class<? extends AbstractLimitStrategy> strategy() default DefaultLimitStrategy.class;

    /**
     * 限流的客户端类型，当前只支持redis
     * @return
     */
    CacheClientType clientType() default CacheClientType.REDIS_TEMPLATE;

    /**
     * 限流的key值，跟type有关
     * @return
     */
    String[] key() default {};

    /**
     * 时间范围，单位秒
     * @return
     */
    int period() default 10;

    /**
     * 限制访问次数，（默认10秒钟之内只能访问10次）
     * @return
     */
    int count() default 10;

}
