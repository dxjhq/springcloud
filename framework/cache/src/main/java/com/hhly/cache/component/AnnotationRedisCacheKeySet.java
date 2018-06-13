package com.hhly.cache.component;

import java.util.Map;
import java.util.Set;

/**
 * @author wangxianchen
 * @create 2017-11-03
 * @desc 注解缓存key的时效设置,由子类去实现
 */
public abstract class AnnotationRedisCacheKeySet {

    /**
     * @desc 设置缓存key的时效,单位:秒
     * @author wangxianchen
     * @create 2017-11-03
     * @return
     */
    public abstract Map<String, Long> getExpiresMap();

    public final Set<String> getCacheNames() {
        return getExpiresMap().keySet();
    }
}
