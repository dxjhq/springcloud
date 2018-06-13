package com.hhly.cache.config;

import com.hhly.cache.component.AnnotationRedisCacheKeySet;
import com.hhly.cache.component.CustomRedisCachePrefix;
import com.hhly.cache.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.*;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
* @author wangxianchen
* @create 2017-07-25
* @desc redis全局配置
*/
@EnableCaching
@Configuration
public class CacheConfigurer extends CachingConfigurerSupport {

    //@Resource(name = "redisTemplate")
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private Environment env;

    @Autowired(required = false)
    private AnnotationRedisCacheKeySet annotationRedisCacheKeySet;

    /**
     * 生成key的策略
     * @return
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(":");
                sb.append(method.getName());
                for (Object obj : params) {
                    if(obj != null){
                        sb.append("_");
                        sb.append(obj.toString());
                    }
                }
                return sb.toString();
            }
        };
    }

    /**
     * 管理缓存
     */
    @Bean
    public CacheManager cacheManager() {
        StringBuffer prefix = new StringBuffer();
        prefix.append(RedisConstant.ANNOTATION_CACHE_PREFIX);
        prefix.append(env.getProperty("spring.application.name"));
        prefix.append(":");
        RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
        rcm.setUsePrefix(true);
        rcm.setCachePrefix(new CustomRedisCachePrefix(prefix.toString())); //默认为:
        if(annotationRedisCacheKeySet != null){
            rcm.setCacheNames(annotationRedisCacheKeySet.getCacheNames());
            rcm.setExpires(annotationRedisCacheKeySet.getExpiresMap());
        }
        rcm.setDefaultExpiration(RedisConstant.DEFAULT_CACHE_KEY_EXPIRATION); //默认过期时间为5天
        return rcm;
    }
}