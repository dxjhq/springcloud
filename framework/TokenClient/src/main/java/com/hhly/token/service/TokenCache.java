package com.hhly.token.service;

import com.google.gson.Gson;
import com.hhly.cache.service.RedisServiceToken;
import com.hhly.token.component.SpringContextUtil;
import com.hhly.token.constant.Constant;
import com.hhly.token.model.LocalProfile;

/**
 * @author pengchao
 * @create 2017-12-14
 * @desc
 */
public class TokenCache {
    //@Autowired
    //RedisServiceToken redisServiceToken;
    private final static Gson gson = new Gson();
    private final static RedisServiceToken redisServiceToken = SpringContextUtil.getBean(RedisServiceToken.class);

    private static String getTokenKey(String key){
        return Constant.CACHE_KEY_PREFIX_TOKEN + Constant.CACHE_KEY_PREFIX_PROFILE + key;
    }

    /**
     * 新增Token
     * @param localProfile
     */
    protected static void set(LocalProfile localProfile) {
        set(localProfile, Constant.TIME_OUT_WEEK);
    }

    /**
     * 新增带过期时间的Token
     * @param localProfile
     * @param timeout
     */
    protected static void set(LocalProfile localProfile, long timeout) {
        String cacheKey = getTokenKey(localProfile.getSid());
        redisServiceToken.set(cacheKey, localProfile.toJson(), timeout);
    }

    /**
     * 刷新Token过期时间
     * @param key
     * @param timeout
     * @return
     */
    protected static boolean resetExpire(String key,Long timeout){
        String cacheKey = getTokenKey(key);
        return redisServiceToken.resetExpire(cacheKey,timeout);
    }

    /**
     * 获取Token数据
     * @param key
     * @return
     */
    protected static LocalProfile get(String key) {
        String cacheKey = getTokenKey(key);
        Object o = redisServiceToken.get(cacheKey);
        if(o == null) return null;

        String json = o.toString();
        LocalProfile localProfile = gson.fromJson(json,LocalProfile.class);
        return localProfile;
    }

    /**
     * 删除Token
     */
    protected static void delete(String key) {
        String cacheKey = getTokenKey(key);
        redisServiceToken.remove(cacheKey);
    }
    protected static void deleteKey(String key) {
        redisServiceToken.remove(key);
    }
}
