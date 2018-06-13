package com.hhly.cache.service;

import com.hhly.cache.exception.ExclusiveLockException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
* @author pengchao
* @create 2017-12-12
* @desc redis通用操作服务
*/
public abstract class AbstractRedisService {
    private Logger logger = LoggerFactory.getLogger(AbstractRedisService.class);

    /**
     * @desc 待子类实现
     * @author wangxianchen
     * @create 2018-01-16
     * @return
     */
    public abstract RedisTemplate<String, Object> getRedisTemplate();

    /**
     * @desc 写入缓存
     * @author wangxianchen
     * @create 2017-07-25
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            valueOperations().set(key, value);
            result = true;
        } catch (Exception e) {
            logger.error("写入redis异常,key={},value={}",key,value);
        }
        return result;
    }
    /**
     * @desc 写入缓存设置时效时间
     * @author wangxianchen
     * @create 2017-07-25
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            valueOperations().set(key,value, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            logger.error("写入redis异常,key={},value={},expireTime={}",key,value,expireTime);
        }
        return result;
    }

    /**
     * @desc 写入缓存设置时效时间
     * @author wangxianchen
     * @create 2017-09-18
     * @param key
     * @param value
     * @param expireTime
     * @param unit
     * @return
     */
    public boolean set(final String key, Object value, long expireTime,final TimeUnit unit) {
        boolean result = false;
        try {
            valueOperations().set(key, value, expireTime, unit);
            result = true;
        } catch (Exception e) {
            logger.error("写入redis异常,key={},value={},expireTime={},TimeUnit={}",key,value,expireTime,unit.toString());
        }
        return result;
    }

    /**
     * @desc 重设key的过期时间,单位:秒
     * @author wangxianchen
     * @create 2017-07-25
     * @param key
     * @param expireTime
     * @return
     */
    public boolean resetExpire(final String key,Long expireTime){
        return getRedisTemplate().expire(key, expireTime, TimeUnit.SECONDS);
    }
    /**
     * @desc 批量删除对应的keys
     * @author wangxianchen
     * @create 2017-07-25
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * @desc 批量删除key
     * @author wangxianchen
     * @create 2017-07-25
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<String> keys = getRedisTemplate().keys(pattern);
        if (keys.size() > 0){
            getRedisTemplate().delete(keys);
        }
    }
    /**
     * @desc 删除对应的key
     * @author wangxianchen
     * @create 2017-07-25
     * @param key
     */
    public void remove(final String key) {
        getRedisTemplate().delete(key);
    }

    /**
     * @desc 批量删除key
     * @author wangxianchen
     * @create 2017-09-18
     * @param keys
     */
    public void remove(final Collection keys) {
        getRedisTemplate().delete(keys);
    }

    /**
     * @desc 判断缓存中是否有对应的key
     * @author wangxianchen
     * @create 2017-07-25
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return getRedisTemplate().hasKey(key);
    }
    /**
     * @desc 读取缓存
     * @author wangxianchen
     * @create 2017-07-25
     * @param key
     * @return
     */
    public Object get(final String key) {
        return valueOperations().get(key);
    }

    /**
     * @desc 根据Key获取对象. 更新了redis value值的序列化方式,可以对JSON字符串直接转对象.之前是使用Gson进行转换的.故此方法已被淘汰.
     * 请使用 get()方法 .updated by wangxianchen 2017-09-19
     * @author wangxianchen
     * @create 2017-07-25
     * @param key
     * @param clazz
     * @return
     */
    @Deprecated
    public Object getByClass(String key, Class clazz){
        try {
            return valueOperations().get(key);
        } catch (Exception e) {
            logger.error("获取对象失败,key={},value={}",key,clazz);
        }
        return null;
    }

    /**
     * @desc 哈希 添加
     * @author wangxianchen
     * @create 2017-07-25
     * @param key
     * @param hashKey
     * @param value
     */
    public void hmSet(String key, String hashKey, Object value){
        hashOperations().put(key,hashKey,value);
    }

    /**
     * @desc 哈希获取数据
     * @author wangxianchen
     * @create 2017-07-25
     * @param key
     * @param hashKey
     * @return
     */
    public Object hmGet(String key, Object hashKey){
        return hashOperations().get(key,hashKey);
    }

    /**
     * @desc 列表添加
     * @author wangxianchen
     * @create 2017-07-25
     * @param k
     * @param v
     */
    public void lPush(String k,Object v){
        listOperations().rightPush(k,v);
    }

    /**
     * @desc 列表获取
     * @author wangxianchen
     * @create 2017-07-25
     * @param k
     * @param start
     * @param end
     * @return
     */
    public List<Object> lRange(String k, long start, long end){
        return listOperations().range(k,start,end);
    }

    /**
     * @desc 集合添加
     * @author wangxianchen
     * @create 2017-07-25
     * @param key
     * @param value
     */
    public void add(String key,Object value){
        setOperations().add(key,value);
    }

    /**
     * @desc 集合获取
     * @author wangxianchen
     * @create 2017-07-25
     * @param key
     * @return
     */
    public Set<Object> members(String key){
        return setOperations().members(key);
    }

    /**
     * @desc 有序集合添加
     * @author wangxianchen
     * @create 2017-07-25
     * @param key
     * @param value
     * @param score
     */
    public void zAdd(String key,Object value,double score){
        zSetOperations().add(key,value,score);
    }

    /**
     * @desc 有序集合获取
     * @author wangxianchen
     * @create 2017-07-25
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<Object> rangeByScore(String key,double min,double max){
        return zSetOperations().rangeByScore(key, min, max);
    }

    /**
     *
     * @desc 排它锁,使用场景：如防止用户3秒内重复提交订单,若事务T对数据对象A加上X锁，则只允许T读取和修改A，其它任何事务都不能再对A加任何类型的锁，直到T释放A上的锁。
     * @author wangxianchen
     * @create 2017-11-03
     * @param key 上锁的key
     * @param lockSeconds 锁定时间, 在此时间内只能成功有一个锁,其它尝试锁请求直接抛出异常
     * @param bizRemark 业务描述, 如：提交订单
     */
    public void exclusiveLock(String key, int lockSeconds, String bizRemark) throws ExclusiveLockException {
        exclusiveLock(key, lockSeconds, null, bizRemark);
    }

    /**
     * @desc
     * @author wangxianchen
     * @create 2017-11-03
     * @param key 上锁的key
     * @param lockSeconds 锁定时间, 在此时间内只能成功有一个锁,其它尝试锁请求直接抛出异常
     * @param waitTimeout 尝试获取锁的超时时间，单位：毫秒
     * @param bizRemark 业务描述, 如：提交订单
     */
    public  void exclusiveLock(String key, int lockSeconds, Long waitTimeout, String bizRemark)throws ExclusiveLockException {
        if (lockSeconds < 1)
            lockSeconds = 1;
        if (waitTimeout == null || waitTimeout < 1)
            waitTimeout = 1L;
        boolean locked = false;
        long lockSecondsTime = lockSeconds * 1000;
        while (!locked && waitTimeout > 0) {
            long now = System.currentTimeMillis();
            long lockTimeout = now + lockSecondsTime + 1;
            locked = getRedisTemplate().opsForValue().setIfAbsent(key, lockTimeout);

            boolean locked2 = false;
            if (!locked) {
                Object obj1 = getRedisTemplate().opsForValue().get(key);
                long oldValue = obj1 == null ? 0 : (Long)obj1;
                Object obj2 = getRedisTemplate().opsForValue().getAndSet(key, lockTimeout);
                long oldValue2 = obj2 == null ? 0 : (Long)obj2; // 获取旧值并且设置新值
                locked2 = now > oldValue && now > oldValue2; // 防止死锁,如果当前时间大于旧的时间值,则当作正常获取到锁
            }

            if (locked || locked2) { // 顺利获取到锁，或者当前时间值大于比key对应的旧时间值，则说明获取到锁
                locked = getRedisTemplate().expire(key, lockSeconds * 5, TimeUnit.SECONDS); // 设置key在redis的5倍生存时间，过期自动删除，减少内存占用
                break;
            } else {
                waitTimeout -= 100;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!locked) {
            String desc = "获取排它锁失败,key=" + key;
            if(!StringUtils.isEmpty(bizRemark)){
                desc = bizRemark + ":" + desc;
            }
            throw new ExclusiveLockException(desc);
        }
    }



    /**
     * 实例化 HashOperations 对象,可以使用 Hash 类型操作
     * @return
     */
    private HashOperations<String, String, Object> hashOperations() {
        return getRedisTemplate().opsForHash();
    }

    /**
     * 实例化 ValueOperations 对象,可以使用 String 操作
     * @return
     */
    private ValueOperations<String, Object> valueOperations() {
        return getRedisTemplate().opsForValue();
    }

    /**
     * 实例化 ListOperations 对象,可以使用 List 操作
     * @return
     */
    private ListOperations<String, Object> listOperations() {
        return getRedisTemplate().opsForList();
    }

    /**
     * 实例化 SetOperations 对象,可以使用 Set 操作
     * @return
     */
    private SetOperations<String, Object> setOperations() {
        return getRedisTemplate().opsForSet();
    }

    /**
     * 实例化 ZSetOperations 对象,可以使用 ZSet 操作
     * @return
     */
     private ZSetOperations<String, Object> zSetOperations() {
        return getRedisTemplate().opsForZSet();
    }
}