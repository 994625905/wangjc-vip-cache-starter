package vip.wangjc.cache.service;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis相关的操作（五种基本数据类型）
 * @author wangjc
 * @title: RedisService
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/20 - 16:39
 */
public interface RedisService {

    /**
     * 获取当前的RedisTemplate
     * @return
     */
    RedisTemplate getRedisTemplate();

    //================================================String================================================

    /**
     * 设置值，
     * @param key
     * @param value：实体的话需要序列化
     */
    void set(String key, Object value);

    /**
     * 设置值，过期时间，
     * @param key
     * @param value
     * @param expire：精确到秒
     */
    void set(String key, Object value, long expire);

    /**
     * 获取值
     * @param key
     * @param clz
     * @param <T>：泛型
     * @return
     */
    <T> T get(String key, Class<T> clz);

    /**
     * value替换key对应的值
     * @param key
     * @param value
     * @return： 返回旧值
     */
    Object getAndSet(String key, Object value);

    /**
     * 字符串拼接
     * @param key：如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。
     *              如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样。
     * @param value：
     * @return 返回拼接完后字符串的长度
     */
    Integer stringAppend(String key, String value);

    /**
     * 字符串截取
     * @param key
     * @param start：开始索引（包含）
     * @param end：结束索引（不包含）
     * @return 截取后的子字符串
     */
    String stringSubString(String key, long start, long end);

    /**
     * 将key对应的数值增加delta
     * @param key
     * @param delta：也可为负数
     * @return
     */
    Long stringIncrement(String key, long delta);

    /**
     * 将key对应的数值增加delta
     * @param key
     * @param delta
     * @return
     */
    Double stringIncrement(String key, double delta);

    //================================================hash================================================

    /**
     * 查看哈希表 hKey 中，给定域 hashKey 是否存在。
     * @param key 哈希表名称
     * @param hashKey 域的hashKey
     * @return 如果哈希表含有给定域，返回 1 。如果哈希表不含有给定域，或 hashKey 不存在，返回 0 。
     */
    Boolean hashCheckExists(String key, String hashKey);

    /**
     * 获取hash表中hashKey的值
     * @param key
     * @param hashKey
     * @param <T>
     * @return
     */
    <T> T hashGet(String key, Object hashKey);

    /**
     * 给hash表中hashKey设置
     * @param key
     * @param hashKey
     * @param value
     */
    void hashPut(String key, Object hashKey, Object value);

    /**
     * 给hash表中hashKey设置
     * @param key
     * @param hashKey
     * @param value
     * @param time：过期时间，是整个hash表
     */
    void hashPut(String key, Object hashKey, Object value, long time);


    /**
     * 给hash表中依次添加
     * @param key
     * @param map
     */
    void hashPutAll(String key, Map map);

    /**
     * 给hash表中依次添加
     * @param key
     * @param map
     * @param time
     */
    void hashPutAll(String key, Map map, long time);

    /**
     * 获取所有的散列
     * @param key
     * @return
     */
    Map hashGetAll(String key);

    /**
     * 获取hash表中所有的hashKey
     * @param key
     * @return
     */
    Set<Object> hashGetAllKey(String key);

    /**
     * 获取hash表中所有的value
     * @param key
     * @return
     */
    List<Object> hashGetAllValue(String key);

    /**
     * 哈希表 hKey 中的域 hashKey 的值加上增量 delta 。
     * <p>
     * 增量也可以为负数，相当于对给定域进行减法操作。  如果 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。
     * 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。
     * 另外，对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误。
     * @param key
     * @param hashKey
     * @param delta
     * @return
     */
    Long hashIncrement(String key, String hashKey, Long delta);

    /**
     * 哈希表 hKey 中的域 hashKey 的值加上增量 delta 。
     * @param key
     * @param hashKey
     * @param delta
     * @return
     */
    Double hashIncrement(String key, String hashKey, Double delta);

    /**
     * 获取hash表中字段数量
     * @param key
     * @return
     */
    Long hashSize(String key);

    /**
     * 批量删除hash表中的字段值
     * @param key
     * @param hashKey
     * @return 删除成功的条目
     */
    Long hashDelete(String key, Object... hashKey);

    //================================================list================================================

    /**
     * 默认从右向左存压栈
     * @param key
     * @param value
     */
    void listPush(String key, Object value);

    /**
     * 从右向左存压栈
     * @param key
     * @param value
     */
    void listRightPush(String key, Object value);

    /**
     * 从右侧出栈
     * @param key
     * @param <T>
     * @return
     */
    <T> T listRightPop(String key);

    /**
     * 从左向右存压栈
     * @param key
     * @param value
     */
    void listLeftPush(String key, Object value);

    /**
     * 从左侧出栈
     * @param key
     * @param <T>
     * @return
     */
    <T> T listLeftPop(String key);

    /**
     * list长度
     * @param key
     * @return
     */
    Long listSize(String key);

    /**
     * 查询list截取
     * @param key
     * @param start：开始索引，包含
     * @param end：结束索引：不包含
     * @return
     */
    List<Object> listRange(String key, long start, long end);

    /**
     * 裁剪(删除), 删除 除了[start,end]以外的所有元素
     * @param key
     * @param start：开始索引，包含
     * @param end：结束索引：不包含
     */
    void listTrimByRange(String key, long start, long end);

    /**
     * 移除key中值为value的i个,返回删除的个数；如果没有这个元素则返回0
     * @param key
     * @param num
     * @param value
     * @return
     */
    Long listDelete(String key, long num, Object value);

    /**
     * 按指定下标获取
     * @param key
     * @param index
     * @param <T>
     * @return
     */
    <T> T listGet(String key, long index);

    /**
     * 根据指定下标设置值
     * @param key
     * @param index
     * @param value
     */
    void listSet(String key, long index, Object value);

    /**
     * 设置集合，从右向左
     * @param key
     * @param list
     */
    void listSet(String key, List<Object> list);

    /**
     * 设置集合，从右向左，过期时间
     * @param key
     * @param list
     * @param time：精确到秒
     */
    void listSet(String key, List<Object> list, long time);

    //================================================Set================================================

    /**
     * 将一个或多个 value 元素加入到集合 key 当中，已经存在于集合的 value 元素将被忽略。
     * @param key
     * @param values
     * @return
     */
    Long setAdd(String key, Object... values);

    Long setAdd(String key, long time, Object... values);

    /**
     * 获取set集合长度
     * @param key
     * @return
     */
    Long setSize(String key);

    /**
     * 获取set中的元素
     * @param key
     * @return
     */
    Set<Object> setGet(String key);

    /**
     * 检查元素是不是set集合中
     * @param key
     * @param value
     * @return
     */
    Boolean setCheckExists(String key, Object value);

    /**
     * 删除set中元素
     * @param key
     * @param values
     * @return
     */
    Long setDelete(String key, Object[] values);

    //================================================公共方法================================================

    /**
     * 根据key删除
     * @param key
     * @return
     */
    Boolean del(String key);

    /**
     * 根据key批量删除
     * @param key
     */
    void del(String[] key);

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    boolean hasKey(String key);

    /**
     * 获取key过期时间
     * @param key
     * @return
     */
    long getExpire(String key);

    /**
     * 设置key过期时间
     * @param key
     * @param time：精确到秒
     * @return
     */
    void setExpire(String key, long time);
}
