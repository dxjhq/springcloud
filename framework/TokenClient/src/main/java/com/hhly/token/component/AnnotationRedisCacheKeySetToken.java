package com.hhly.token.component;

import com.hhly.cache.component.AnnotationRedisCacheKeySet;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pengchao
 * @create 2017-12-14
 * @desc
 */
@Component
public class AnnotationRedisCacheKeySetToken extends AnnotationRedisCacheKeySet {
    @Override
    public Map<String, Long> getExpiresMap(){
        return new HashMap<String,Long>();
    }
}
