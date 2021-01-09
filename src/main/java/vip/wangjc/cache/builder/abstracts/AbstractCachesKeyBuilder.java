package vip.wangjc.cache.builder.abstracts;

import org.aopalliance.intercept.MethodInvocation;

/**
 * 缓存器key的生成器
 * @author wangjc
 * @title: AbstractCachesKeyBuilder
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 15:24
 */
public abstract class AbstractCachesKeyBuilder {

    /**
     *
     * @param invocation：
     * @param definitionKeys：
     * @return
     */
    /**
     * key的生成方法
     * @param invocation：拦截的方法
     * @param definitionKeys：定义的参数表达式
     * @return
     */
    public abstract String buildKey(MethodInvocation invocation, String[] definitionKeys);

}
