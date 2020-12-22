package vip.wangjc.cache.builder.rewrite;

import vip.wangjc.cache.builder.AbstractCachesExpireBuilder;

/**
 * 随机过期时间，防止缓存雪崩
 * @author wangjc
 * @title: RandomCachesExpireBuilder
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/22 - 14:26
 */
public class RandomCachesExpireBuilder extends AbstractCachesExpireBuilder {

    private static final long min = 12 * 60 * 60; // 12小时
    private static final long max = 24 * 60 * 60; // 24小时

    @Override
    public long expire() {
        return this.getRandom().nextLong(min,max);
    }
}
