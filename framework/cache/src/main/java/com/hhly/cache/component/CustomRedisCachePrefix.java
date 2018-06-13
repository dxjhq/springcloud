package com.hhly.cache.component;

import org.springframework.data.redis.cache.RedisCachePrefix;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author wangxianchen
 * @create 2017-11-03
 * @desc 自定义redisCache key的前缀
 */
public class CustomRedisCachePrefix implements RedisCachePrefix {

    private final RedisSerializer serializer = new StringRedisSerializer();

    private final String delimiter;

    public CustomRedisCachePrefix(String delimiter) {
        this.delimiter = delimiter;
    }

    public byte[] prefix(String cacheName) {
        if(!cacheName.endsWith(":")){
            cacheName = cacheName.concat(":");
        }
        return serializer.serialize(delimiter.concat(cacheName));
    }
}
