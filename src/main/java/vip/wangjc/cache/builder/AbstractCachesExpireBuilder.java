package vip.wangjc.cache.builder;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 过期时间生成器
 * @author wangjc
 * @title: AbstractCachesExpireBuilder
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/22 - 14:22
 */
public abstract class AbstractCachesExpireBuilder {

    /**
     * 过期时间
     * @return
     */
    public abstract long expire();

    /**
     * 获取当前线程的随机生成器
     * @return
     */
    protected ThreadLocalRandom getRandom(){
        return ThreadLocalRandom.current();
    }
}
