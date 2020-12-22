package vip.wangjc.cache;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import vip.wangjc.cache.annotation.Limiter;
import vip.wangjc.cache.entity.LimitEntity;
import vip.wangjc.cache.execute.AbstractLimitExecute;
import vip.wangjc.cache.execute.LimitExecuteFactory;
import vip.wangjc.cache.util.CacheUtil;

/**
 * 限流的模板方法
 * @author wangjc
 * @title: LimitTemplate
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 17:22
 */
public class LimitTemplate {

    private static final Logger logger = LoggerFactory.getLogger(LimitTemplate.class);

    private static final String JVM_PROCESSId = CacheUtil.getJVMProcessId();
    private static final String LOCAL_MAC = CacheUtil.getLocalMac();

    /** 限流执行器的构建工厂 */
    @Autowired(required = false)
    private LimitExecuteFactory limitExecuteFactory;

    /**
     * 执行限流
     * @param methodInvocation
     * @param limiter
     * @return
     */
    public LimitEntity limit(MethodInvocation methodInvocation, Limiter limiter) throws Exception {

        if(this.limitExecuteFactory == null){
            logger.error("limitExecuteFactory is null,unable limit");
            return null;
        }
        /** 构建key */
        String key = limiter.keyBuilder().newInstance().buildKey(methodInvocation, JVM_PROCESSId, LOCAL_MAC, limiter.key());

        /** 构建限流执行器 */
        AbstractLimitExecute limitExecute = this.limitExecuteFactory.buildExecute(limiter);

        /** 限流 */
        Boolean pass = limitExecute.pass(key, limiter.period(), limiter.count());

        /** 限流后置策略 */
        if(!pass){
            return new LimitEntity(key,limiter.period(),limiter.count(),limitExecute);
        }
        return null;
    }

    /**
     * 没有通过的后置策略
     * @param limitEntity
     * @param limiter
     */
    public Object noPass(LimitEntity limitEntity, Limiter limiter){
        try {
            return limiter.strategy().newInstance().strategy(limitEntity);
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("noPass error,strategy[{}]",limiter.strategy().getName());
            return null;
        }
    }

}
