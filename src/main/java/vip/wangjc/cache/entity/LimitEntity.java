package vip.wangjc.cache.entity;

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

    public LimitEntity(String key, Integer period, Integer count, AbstractLimitExecute execute) {
        this.key = key;
        this.period = period;
        this.count = count;
        this.execute = execute;
    }

    /**
     * 限流的key值
     */
    private String key;

    /**
     * 限流时间单位：秒
     */
    private Integer period;

    /**
     * period单位内限流次数
     */
    private Integer count;

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

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public AbstractLimitExecute getExecute() {
        return execute;
    }

    public void setExecute(AbstractLimitExecute execute) {
        this.execute = execute;
    }
}
