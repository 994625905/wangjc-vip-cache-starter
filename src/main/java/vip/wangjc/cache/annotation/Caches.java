package vip.wangjc.cache.annotation;

import vip.wangjc.cache.builder.AbstractCachesExpireBuilder;
import vip.wangjc.cache.builder.AbstractCachesKeyBuilder;
import vip.wangjc.cache.builder.rewrite.DefaultCachesKeyBuilder;
import vip.wangjc.cache.builder.rewrite.RandomCachesExpireBuilder;
import vip.wangjc.cache.entity.CacheClientType;

import java.lang.annotation.*;

/**
 * 缓存器
 * @author wangjc
 * @title: Cacher
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/22 - 9:27
 */
@Target({ElementType.METHOD}) // 只作用于方法
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Caches {

    /**
     * 缓存的客户端类型，当前只支持redis
     * @return
     */
    CacheClientType clientType() default CacheClientType.REDIS_TEMPLATE;

    /**
     * 缓存器的key，支持EL表达式
     * @return
     */
    String[] keys() default {};

    /**
     * 缓存器的key构建器
     * @return
     */
    Class<? extends AbstractCachesKeyBuilder> keyBuilder() default DefaultCachesKeyBuilder.class;

    /**
     * 过期时间的生成器，默认为随机
     * @return
     */
    Class<? extends AbstractCachesExpireBuilder> expireBuilder() default RandomCachesExpireBuilder.class;

}
