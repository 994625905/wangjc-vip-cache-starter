package vip.wangjc.cache.limit.builder;

import org.aopalliance.intercept.MethodInvocation;

/**
 * 限流器key的生成器
 * @author wangjc
 * @title: ILimitKeyBuilder
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 15:24
 */
public abstract class AbstractLimitKeyBuilder {

    /**
     *
     * @param invocation：
     * @param definitionKeys：
     * @return
     */
    /**
     * key的生成方法
     * @param invocation：拦截的方法
     * @param jvmProcessId：当前JVM进程id
     * @param localMac：本机网卡
     * @param definitionKeys：定义的参数表达式
     * @return
     */
    public abstract String buildKey(MethodInvocation invocation, String jvmProcessId, String localMac, String[] definitionKeys);

}
