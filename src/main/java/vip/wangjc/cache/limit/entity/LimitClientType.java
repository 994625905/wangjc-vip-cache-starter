package vip.wangjc.cache.limit.entity;

/**
 * 限流器的客户端类型：目前只提供redisTemplate
 * @author wangjc
 * @title: LimitClient
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 18:07
 */
public enum LimitClientType {

    /**
     * spring提供的Redis template
     */
    REDIS_TEMPLATE(1,"spring redis template");

    private Integer type;
    private String msg;

    private LimitClientType(Integer type, String msg) {
        this.type = type;
        this.msg = msg;
    }

}
