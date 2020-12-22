package vip.wangjc.cache.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.wangjc.cache.LimitTemplate;
import vip.wangjc.cache.annotation.Limiter;
import vip.wangjc.cache.entity.LimitEntity;

/**
 * 限流器的aop切面处理器
 * @author wangjc
 * @title: LimitInterceptor
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 19:14
 */
public class LimitInterceptor implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LimitInterceptor.class);
    private final LimitTemplate limitTemplate;

    public LimitInterceptor(LimitTemplate limitTemplate){
        this.limitTemplate = limitTemplate;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {
            Limiter limiter = methodInvocation.getMethod().getAnnotation(Limiter.class);
            LimitEntity limitEntity = this.limitTemplate.limit(methodInvocation, limiter);

            if(limitEntity == null){
                return methodInvocation.proceed();
            }
            return this.limitTemplate.noPass(limitEntity,limiter);
        }catch (Exception e){
            logger.error("LimitInterceptor invoke,reason:[{}]",e.getMessage());
            throw e;
        }
    }
}
