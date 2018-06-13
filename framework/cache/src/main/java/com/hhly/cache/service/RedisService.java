package com.hhly.cache.service;

import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author pengchao
* @create 2017-12-12
* @desc Redis 默认连接服务
*/
@Service("redisService")
public class RedisService extends AbstractRedisService {

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    @Override
    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }
}