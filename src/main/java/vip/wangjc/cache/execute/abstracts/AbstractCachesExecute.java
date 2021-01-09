package vip.wangjc.cache.execute.abstracts;

/**
 * 缓存器
 * @author wangjc
 * @title: ILimitService
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 15:47
 */
public abstract class AbstractCachesExecute {

    /**
     * 缓存器的key前缀
     */
    private static final String KEY_PREFIX = "caches";

    /**
     * 构造key
     * @param key
     * @return
     */
    protected String key(String key){
        StringBuffer sb = new StringBuffer(KEY_PREFIX);
        sb.append("@");
        sb.append(key);
        return sb.toString();
    }

    /**
     * 检查缓存
     * @param key
     * @param expire
     * @return
     */
    public abstract Object check(String key, long expire);

    /**
     * 设置缓存
     * @param value
     * @param key
     * @param expire
     */
    public abstract void set(Object value, String key, long expire);

    /**
     * 删除缓存
     * @param key
     */
    public abstract void del(String key);

}
