package com.hhly.cache.component;

import com.hhly.cache.constant.RedisConstant;
import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author wangxianchen
 * @create 2017-09-15
 * @desc redis实现mybatis的二级缓存
 */
public class MybatisRedisCache implements Cache,ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(MybatisRedisCache.class);
    
    private static ApplicationContext applicationContext;

    public final void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext; // NOSONAR
    }

    // 读写锁
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    private RedisTemplate<String, Object> redisTemplate;

    private String id;

    private static String keyPrefix;

    private void setKeyPrefix(){
        keyPrefix = RedisConstant.MYBATIS_CACHE_PREFIX+ applicationContext.getEnvironment().getProperty("spring.application.name")+":";
    }

    private void setRedisTemplate(){
        redisTemplate = (RedisTemplate)applicationContext.getBean("redisTemplate");
    }
    public MybatisRedisCache(){
        logger.info("initialization MybatisRedisCache bean");
    }
    public MybatisRedisCache(final String id) {
        setKeyPrefix();
        setRedisTemplate();
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        logger.info("Redis Cache id " + id);
        this.id = id;
    }

    @Override
    public final String getId() {
        return this.id;
    }

    @Override
    public final void putObject(Object key, Object value) {

        if (value != null) {
            if(value instanceof List){
                //if(AssembleUtils.isEmpty((List)value))
                if(((List)value).size()<=0){
                    return;
                }
            }
            if(value instanceof Map){
                //if(AssembleUtils.isEmpty((Map)value)){
                if(((Map)value).size()<=0){
                    return;
                }
            }
            //logger.info("putObject:key={},value={}",keyPrefix+key.toString(),value);
            // 向Redis中添加数据，有效时间是30天
            redisTemplate.opsForValue().set(keyPrefix+key.toString(), value, RedisConstant.DEFAULT_CACHE_KEY_EXPIRATION, TimeUnit.SECONDS);
        }
    }

    @Override
    public final Object getObject(Object key) {
        try {
            if (key != null) {
                //logger.info("getObject:{}",keyPrefix+key.toString());
                return redisTemplate.opsForValue().get(keyPrefix+key.toString());
            }
        } catch (Exception e) {
            logger.error("从MybatisRedisCache中获取值时异常 key={}",key.toString());
        }
        return null;
    }

    @Override
    public final Object removeObject(Object key) {
        try {
            if (key != null) {
                //logger.info("removeObject:{}",keyPrefix+key.toString());
                redisTemplate.delete(keyPrefix+key.toString());
            }
        } catch (Exception e) {
            logger.error("移除MybatisRedisCache key{}异常",key.toString());
        }
        return null;
    }

    @Override
    public final void clear() {
        logger.debug("清空缓存");
        try {
            Set<String> keys = redisTemplate.keys(keyPrefix+"*" + this.id + "*");
            if (!CollectionUtils.isEmpty(keys)) {
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
            logger.error("清空MybatisRedisCache异常");
        }
    }

    @Override
    public final int getSize() {
        Long size = (Long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
        return size.intValue();
    }

    @Override
    public final ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }
}