package vip.wangjc.cache;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import vip.wangjc.cache.annotation.Caches;
import vip.wangjc.cache.annotation.Flush;
import vip.wangjc.cache.execute.abstracts.AbstractCachesExecute;
import vip.wangjc.cache.execute.factory.CachesExecuteFactory;

/**
 * 缓存器的模板方法
 * @author wangjc
 * @title: LimitTemplate
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 17:22
 */
public class CachesTemplate {

    private static final Logger logger = LoggerFactory.getLogger(CachesTemplate.class);

    /** 缓存执行器的构建工厂 */
    @Autowired(required = false)
    private CachesExecuteFactory cachesExecuteFactory;

    /**
     * 检验缓存
     * @param methodInvocation
     * @param caches
     * @return
     */
    public Object checkCache(MethodInvocation methodInvocation, Caches caches) throws Exception {

        if(this.cachesExecuteFactory == null){
            logger.error("cachesExecuteFactory is null,unable checkCache");
            return null;
        }
        /** 构建key */
        String key = caches.keyBuilder().newInstance().buildKey(methodInvocation, caches.keys());

        /** 构建缓存执行器 */
        AbstractCachesExecute cachesExecute = this.cachesExecuteFactory.buildExecute(caches);

        /** 构建有效时间 */
        long expire = caches.expireBuilder().newInstance().expire();

        /** 缓存检查与设置 */
        Object check = cachesExecute.check(key, expire);
        if(check == null){
            try {
                check = methodInvocation.proceed();
                cachesExecute.set(check, key, expire);
            } catch (Throwable t) {
                logger.error("target method execution failed! reason:[{}]",t.getMessage());
                t.printStackTrace();
            }
        }
        return check;
    }

    /**
     * 刷新缓存--先删除缓存，在更新数据库
     * @param methodInvocation
     * @param flush
     * @return
     */
    public Object flush(MethodInvocation methodInvocation, Flush flush) throws Exception {

        if(this.cachesExecuteFactory == null){
            logger.error("cachesExecuteFactory is null,unable flush");
            return null;
        }
        /** 构建key */
        String key = flush.keyBuilder().newInstance().buildKey(methodInvocation, flush.keys());

        /** 构建缓存执行器 */
        AbstractCachesExecute cachesExecute = this.cachesExecuteFactory.buildExecute(flush);

        Object value = null;
        /** 先删除缓存，在更新数据库 */
        try {
            cachesExecute.del(key);
            value = methodInvocation.proceed();
        } catch (Throwable t) {
            logger.error("target method execution failed! reason:[{}]",t.getMessage());
            t.printStackTrace();
        }
        return value;
    }

}
