package vip.wangjc.cache.service.impl;

import org.springframework.util.CollectionUtils;
import vip.wangjc.cache.client.redis.CacheRedisTemplate;
import vip.wangjc.cache.service.RedisService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis相关的操作（五种基本数据类型）
 * @author wangjc
 * @title: RedisServiceImpl
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/20 - 16:42
 */
public class RedisServiceImpl implements RedisService {

    private final CacheRedisTemplate cacheRedisTemplate;

    public RedisServiceImpl(CacheRedisTemplate cacheRedisTemplate){
        this.cacheRedisTemplate = cacheRedisTemplate;
    }

    //================================================String================================================

    @Override
    public CacheRedisTemplate getCacheRedisTemplate() {
        return this.cacheRedisTemplate;
    }

    @Override
    public void set(String key, Object value) {
        this.cacheRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(String key, Object value, long expire) {
        this.cacheRedisTemplate.opsForValue().set(key, value,expire , TimeUnit.SECONDS);
    }

    @Override
    public <T> T get(String key, Class<T> clz) {
        Object res = this.cacheRedisTemplate.opsForValue().get(key);
        if(res != null){
            return (T)res;
        }
        return null;
    }

    @Override
    public Object getAndSet(String key, Object value) {
        return this.cacheRedisTemplate.opsForValue().getAndSet(key, value);
    }

    @Override
    public Integer stringAppend(String key, String value) {
        return this.cacheRedisTemplate.opsForValue().append(key, value);
    }

    @Override
    public String stringSubString(String key, long start, long end) {
        return this.cacheRedisTemplate.opsForValue().get(key, start, end);
    }

    @Override
    public Long stringIncrement(String key, long delta) {
        return this.cacheRedisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public Double stringIncrement(String key, double delta) {
        return this.cacheRedisTemplate.opsForValue().increment(key, delta);
    }

    //================================================hash================================================

    @Override
    public Boolean hashCheckExists(String key, String hashKey) {
        return this.cacheRedisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public <T> T hashGet(String key, Object hashKey) {
        Object res = this.cacheRedisTemplate.opsForHash().get(key, hashKey);
        if(res != null){
            return (T)res ;
        }
        return null;
    }

    @Override
    public void hashPut(String key, Object hashKey, Object value) {
        this.cacheRedisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public void hashPut(String key, Object hashKey, Object value, long time) {
        this.cacheRedisTemplate.opsForHash().put(key, hashKey, value);
        if(time > 0){
            this.cacheRedisTemplate.expire(key,time,TimeUnit.SECONDS);
        }
    }

    @Override
    public void hashPutAll(String key, Map map) {
        this.cacheRedisTemplate.opsForHash().putAll(key,map);
    }

    @Override
    public void hashPutAll(String key, Map map, long time) {
        this.cacheRedisTemplate.opsForHash().putAll(key,map);
        if(time > 0){
            this.cacheRedisTemplate.expire(key,time,TimeUnit.SECONDS);
        }
    }

    @Override
    public Map hashGetAll(String key) {
        return this.cacheRedisTemplate.opsForHash().entries(key);
    }

    @Override
    public Set<Object> hashGetAllKey(String key) {
        return this.cacheRedisTemplate.opsForHash().keys(key);
    }

    @Override
    public List<Object> hashGetAllValue(String key) {
        return this.cacheRedisTemplate.opsForHash().values(key);
    }

    @Override
    public Long hashIncrement(String key, String hashKey, Long delta) {
        return this.cacheRedisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    @Override
    public Double hashIncrement(String key, String hashKey, Double delta) {
        return this.cacheRedisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    @Override
    public Long hashSize(String key) {
        return this.cacheRedisTemplate.opsForHash().size(key);
    }

    @Override
    public Long hashDelete(String key, Object... hashKey) {
        return this.cacheRedisTemplate.opsForHash().delete(key, hashKey);
    }

    //================================================list================================================

    @Override
    public void listPush(String key, Object value) {
        this.cacheRedisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public void listRightPush(String key, Object value) {
        this.cacheRedisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public <T> T listRightPop(String key) {
        Object res = this.cacheRedisTemplate.opsForList().rightPop(key);
        if(res != null){
            return (T)res;
        }
        return null;

    }

    @Override
    public void listLeftPush(String key, Object value) {
        this.cacheRedisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public <T> T listLeftPop(String key) {
        Object res = this.cacheRedisTemplate.opsForList().leftPop(key);
        if(res != null){
            return (T)res;
        }
        return null;
    }

    @Override
    public Long listSize(String key) {
        return this.cacheRedisTemplate.opsForList().size(key);
    }

    @Override
    public List<Object> listRange(String key, long start, long end) {
        return this.cacheRedisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public void listTrimByRange(String key, long start, long end) {
        this.cacheRedisTemplate.opsForList().trim(key, start, end);
    }

    @Override
    public Long listDelete(String key, long num, Object value) {
        return this.cacheRedisTemplate.opsForList().remove(key, num, value);
    }

    @Override
    public <T> T listGet(String key, long index) {
        Object res = this.cacheRedisTemplate.opsForList().index(key, index);
        if(res != null){
            return (T)res;
        }
        return null;
    }

    @Override
    public void listSet(String key, long index, Object value) {
        this.cacheRedisTemplate.opsForList().set(key, index, value);
    }

    @Override
    public void listSet(String key, List<Object> list) {
        if(list == null || list.size() == 0){
            this.cacheRedisTemplate.delete(key);
        }else{
            this.cacheRedisTemplate.opsForList().rightPushAll(key, list);
        }
    }

    @Override
    public void listSet(String key, List<Object> list, long time) {
        if(list == null || list.size() == 0){
            this.cacheRedisTemplate.delete(key);
        }else{
            this.cacheRedisTemplate.opsForList().rightPushAll(key, list);
            if (time > 0) this.cacheRedisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    //================================================Set================================================

    @Override
    public Long setAdd(String key, Object... values) {
        return this.cacheRedisTemplate.opsForSet().add(key, values);
    }

    @Override
    public Long setAdd(String key, long time, Object... values) {
        Long res = this.cacheRedisTemplate.opsForSet().add(key, values);
        if(time > 0){
            this.cacheRedisTemplate.expire(key,time, TimeUnit.SECONDS);
        }
        return res;
    }

    @Override
    public Long setSize(String key) {
        return this.cacheRedisTemplate.opsForSet().size(key);
    }

    @Override
    public Set<Object> setGet(String key) {
        return this.cacheRedisTemplate.opsForSet().members(key);
    }

    @Override
    public Boolean setCheckExists(String key, Object value) {
        return this.cacheRedisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public Long setDelete(String key, Object[] values) {
        Long remove = this.cacheRedisTemplate.opsForSet().remove(key, values);
        return remove;
    }

    //================================================公共方法================================================

    @Override
    public Boolean del(String key) {
        return this.cacheRedisTemplate.delete(key);
    }

    @Override
    public void del(String[] key) {
        if(key != null && key.length > 0){
            if(key.length == 1){
                this.cacheRedisTemplate.delete(key[0]);
            }else{
                this.cacheRedisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    @Override
    public boolean hasKey(String key) {
        return this.cacheRedisTemplate.hasKey(key);
    }

    @Override
    public long getExpire(String key) {
        return this.cacheRedisTemplate.getExpire(key,TimeUnit.SECONDS);
    }

    @Override
    public void setExpire(String key, long time) {
        if(time > 0){
            this.cacheRedisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

}
