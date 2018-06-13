package com.hhly.utils.json;

import com.google.common.collect.Maps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;

/**
 * @author yuonch
 */
public class SerializerUtil {
    private static Logger logger = LoggerFactory.getLogger(SerializerUtil.class);

    private static Map<Class<?>, Jackson2JsonRedisSerializer<?>> jsonSerializerMap = Maps.newHashMap();

    private static JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer();

    private static StringRedisSerializer stringSerializer = new StringRedisSerializer();

    public static <T> Jackson2JsonRedisSerializer<T> getJsonSerializer(Class<T> clazz) {
        @SuppressWarnings("unchecked") Jackson2JsonRedisSerializer<T> serializer = (Jackson2JsonRedisSerializer<T>) jsonSerializerMap.get(clazz);

        if (serializer == null) {
            serializer = new Jackson2JsonRedisSerializer<>(clazz);
            serializer.setObjectMapper(JsonUtil.getMapper());
            jsonSerializerMap.put(clazz, serializer);
        }

        return serializer;
    }

    public static JdkSerializationRedisSerializer getJdkSerializer() {
        return jdkSerializer;
    }

    public static StringRedisSerializer getStringSerializer() {
        return stringSerializer;
    }
}
