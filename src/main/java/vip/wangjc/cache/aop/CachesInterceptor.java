package vip.wangjc.cache.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.wangjc.cache.CachesTemplate;
import vip.wangjc.cache.annotation.Caches;

/**
 * 缓存器的aop切面处理器
 * @author wangjc
 * @title: CachesInterceptor
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 19:14
 */
public class CachesInterceptor implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CachesInterceptor.class);
    private final CachesTemplate cachesTemplate;

    public CachesInterceptor(CachesTemplate cachesTemplate){
        this.cachesTemplate = cachesTemplate;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {
            Caches caches = methodInvocation.getMethod().getAnnotation(Caches.class);
            return this.cachesTemplate.checkCache(methodInvocation,caches);
        }catch (Exception e){
            logger.error("CachesInterceptor invoke,reason:[{}]",e.getMessage());
            throw e;
        }
    }
}
