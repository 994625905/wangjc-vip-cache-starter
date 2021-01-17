package vip.wangjc.cache.entity;

import vip.wangjc.cache.annotation.Limiter;
import vip.wangjc.cache.execute.abstracts.AbstractLimitExecute;

import java.io.Serializable;

/**
 * 限流组成的实体
 * @author wangjc
 * @title: LimitEntity
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 17:59
 */
public class LimitEntity implements Serializable {

    private static final long serialVersionUID = 7833843471732216083L;

    public LimitEntity(String key, Limiter limiter, AbstractLimitExecute execute) {
        this.key = key;
        this.limiter = limiter;
        this.execute = execute;
    }

    /**
     * 限流key
     */
    private String key;

    /**
     * 限流注解
     */
    private Limiter limiter;

    /**
     * 限流执行器
     */
    private AbstractLimitExecute execute;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Limiter getLimiter() {
        return limiter;
    }

    public void setLimiter(Limiter limiter) {
        this.limiter = limiter;
    }

    public AbstractLimitExecute getExecute() {
        return execute;
    }

    public void setExecute(AbstractLimitExecute execute) {
        this.execute = execute;
    }
}
