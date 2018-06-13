package com.hhly.cache.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

/**
* @author pengchao
* @create 2017-12-12
* @desc redis默认配置
*/
@Configuration
//public class RedisConfigDefault extends CacheConfigurerDefault {
public class RedisConfig {
    /**
     * spring redis配置项
     */
    @Value("${spring.redis.database:0}")
    private int database;
    @Value("${spring.redis.host:}")
    private String host;
    @Value("${spring.redis.port:0}")
    private int port;
    @Value("${spring.redis.password:}")
    private String password;
    @Value("${spring.redis.cluster.nodes:}")
    private String clusterNodes;
    @Value("${spring.redis.timeout:2000}")
    private int timeout;
    //最大空闲连接数, 默认8个
    @Value("${spring.redis.pool.max-idle:100}")
    private int maxidle;
    //最小空闲连接数, 默认0
    @Value("${spring.redis.pool.min-idle:0}")
    private int minidle;
    //最大连接数, 默认8个
    @Value("${spring.redis.pool.max-active:1000}")
    private int maxActive;
    //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
    @Value("${spring.redis.pool.max-wait:1000}")
    private long maxWait;

    //@Bean(name = "tokenRedisClusterConfiguration")
    private RedisClusterConfiguration getClusterConfiguration(String _nodes, int _timeout) {
        Map<String, Object> source = new HashMap<String, Object>();
        source.put("spring.redis.cluster.nodes", _nodes);
        source.put("spring.redis.timeout", _timeout);
        //source.put("spring.redis.cluster.max-redirects", redirects);
        return new RedisClusterConfiguration(new MapPropertySource("redisClusterConfiguration", source));
    }

    private JedisPoolConfig bulidPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxidle);
        config.setMinIdle(minidle);
        config.setMaxTotal(maxActive);
        config.setMaxWaitMillis(maxWait);
        return config;
    }

    @Primary
    @Bean(name = "jedisConnectionFactory")
    public JedisConnectionFactory getConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        if (StringUtils.isEmpty(clusterNodes)) {
            factory.setDatabase(database);
            factory.setHostName(host);
            factory.setPort(port);
        } else {
            factory = new JedisConnectionFactory(getClusterConfiguration(clusterNodes, timeout));
        }

        factory.setPassword(password);
        factory.setTimeout(timeout);
        factory.setPoolConfig(bulidPoolConfig());
        return factory;
    }

    @Primary
    @Bean(name = "jedisClusterConnection")
    public JedisClusterConnection getJedisClusterConnection() {
        if (StringUtils.isEmpty(clusterNodes)) {
            return null;
        } else {
            return (JedisClusterConnection) getConnectionFactory().getConnection();
        }
    }

    @Primary
    @Bean(name = "redisTemplate")
    public RedisTemplate redisTemplateDefault() {
        RedisTemplate redisTemplate = new RedisTemplate();

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        redisTemplate.setConnectionFactory(getConnectionFactory());
        // 设置键（key）的序列化采用StringRedisSerializer。
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 设置值（value）的序列化采用Jackson2JsonRedisSerializer。
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
    /**
     //注入 RedisConnectionFactory
    @Autowired
    RedisConnectionFactory redisConnectionFactory;

     //实例化 RedisTemplate 对象
    @Primary
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // 设置键（key）的序列化采用StringRedisSerializer。
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 设置值（value）的序列化采用Jackson2JsonRedisSerializer。
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    */
}