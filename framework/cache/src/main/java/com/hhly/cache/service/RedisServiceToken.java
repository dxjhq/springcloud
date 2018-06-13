package com.hhly.cache.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author pengchao
* @create 2017-12-12
* @desc Redis Token连接服务
*/
@Service("redisServiceToken")
public class RedisServiceToken extends AbstractRedisService {
//    @Resource(name = "tokenRedisTemplate")
//    private RedisTemplate tokenRedisTemplate;

    private RedisTemplate tokenRedisTemplate;

    @Resource(name = "tokenRedisTemplate")
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.tokenRedisTemplate = redisTemplate;
    }

    @Override
    public RedisTemplate getRedisTemplate() {
        return tokenRedisTemplate;
    }
}