package vip.wangjc.cache.entity;

/**
 * 限流类型
 * @author wangjc
 * @title: LimitType
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/22 - 16:46
 */
public enum LimitType {

    IP(1,"limit by request ip"),
    PARAM(2,"limit by request ip"),
    NORMAL(3,"limit by request ip"),;

    private Integer type;
    private String msg;

    private LimitType(Integer type, String msg) {
        this.type = type;
        this.msg = msg;
    }

}
