package vip.wangjc.cache.execute.rewrite;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import vip.wangjc.cache.client.redis.CacheRedisTemplate;
import vip.wangjc.cache.execute.AbstractLimitExecute;

/**
 * redis限流器
 * @author wangjc
 * @title: RedisLimitExecuteServiceImpl
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 17:18
 */
public class RedisLimitExecute extends AbstractLimitExecute {

    private static final Logger logger = LoggerFactory.getLogger(RedisLimitExecute.class);

    private final CacheRedisTemplate<String,Object> cacheRedisTemplate;

    public RedisLimitExecute(CacheRedisTemplate cacheRedisTemplate){
        this.cacheRedisTemplate = cacheRedisTemplate;
    }

    /**
     * 限流脚本
     * @return
     */
    public String limitScript(){
        return "local c" +
                "\nc = redis.call('get',KEYS[1])" +
                "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
                "\nreturn c;" +
                "\nend" +
                "\nc = redis.call('incr',KEYS[1])" +
                "\nif tonumber(c) == 1 then" +
                "\nredis.call('expire',KEYS[1],ARGV[2])" +
                "\nend" +
                "\nreturn c;";
    }

    @Override
    public Boolean pass(String key, int period, int count) {

        try {
            /** 构造不可变的安全list */
            ImmutableList<String> keyList = ImmutableList.of(key);
            /** 构造Redis脚本 */
            RedisScript<Long> script = new DefaultRedisScript<>(this.limitScript(),Long.class);
            /** 执行脚本 */
            Long res = this.cacheRedisTemplate.execute(script, keyList, count, period);
            if(res != null && res.intValue() <= count){
                return true;
            }
            return false;
        }catch (Exception e){
            logger.error("RedisLimitExecute pass error");
            return false;
        }
    }
}
