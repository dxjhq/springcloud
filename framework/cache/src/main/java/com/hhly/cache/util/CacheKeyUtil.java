package com.hhly.cache.util;

import com.hhly.cache.model.CacheKey;

/**
 * @author pengchao
 * @create 2017-12-27
 * @desc 生成缓存key
 */
public class CacheKeyUtil {
    public static String getCacheKey(CacheKey key){
        String cacheKey =  key.getServerName() + ":" + key.getModuleName() + ":" + key.getBussinessId();
        return MD5Util.encode(cacheKey);
    }
}