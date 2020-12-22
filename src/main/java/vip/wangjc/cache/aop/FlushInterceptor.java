package vip.wangjc.cache.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.wangjc.cache.CachesTemplate;
import vip.wangjc.cache.annotation.Flush;

/**
 * 刷新的aop切面处理器
 * @author wangjc
 * @title: CachesInterceptor
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 19:14
 */
public class FlushInterceptor implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(FlushInterceptor.class);
    private final CachesTemplate cachesTemplate;

    public FlushInterceptor(CachesTemplate cachesTemplate){
        this.cachesTemplate = cachesTemplate;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {
            Flush flush = methodInvocation.getMethod().getAnnotation(Flush.class);
            return this.cachesTemplate.flush(methodInvocation,flush);
        }catch (Exception e){
            logger.error("FlushInterceptor invoke,reason:[{}]",e.getMessage());
            throw e;
        }
    }
}
