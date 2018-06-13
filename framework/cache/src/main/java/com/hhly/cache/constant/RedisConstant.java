package com.hhly.cache.constant;

/**
 * @author pengchao
 * @create 2017-12-06
 * @desc redis操作相关常量池
 */
public class RedisConstant {
    public static final String ANNOTATION_CACHE_PREFIX = "annotation_cache:";

    //默认缓存key的失效时间为30天,单位:秒
    public static final long DEFAULT_CACHE_KEY_EXPIRATION = 2592000L; //30天

    public static final String MYBATIS_CACHE_PREFIX = "mybatis_cache:";

    public static final String EMPTY = "";
}