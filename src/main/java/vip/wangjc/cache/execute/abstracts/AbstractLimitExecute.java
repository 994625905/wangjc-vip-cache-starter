package vip.wangjc.cache.execute.abstracts;

/**
 * 限流器
 * @author wangjc
 * @title: ILimitService
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 15:47
 */
public abstract class AbstractLimitExecute {

    /**
     * 放行
     * @param key
     * @param period
     * @param count
     * @return
     */
    public abstract Boolean pass(String key, int period, int count);
}
