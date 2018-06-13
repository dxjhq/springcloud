package com.hhly.cache.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pengchao
 * @create 2017-12-08
 * @desc Redis默认配置类
 */
@Configuration
public class RedisConfigToken {
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

    /**
     * token redis配置项，值为空时使用默认配置项
     */
    @Value("${token.redis.database:0}")
    private int token_database;
    @Value("${token.redis.host:}")
    private String token_host;
    @Value("${token.redis.port:0}")
    private int token_port;
    @Value("${token.redis.password:}")
    private String token_password;
    @Value("${token.redis.cluster.nodes:}")
    private String token_clusterNodes;
    @Value("${token.redis.timeout:2000}")
    private int token_timeout;
    //最大空闲连接数, 默认8个
    @Value("${token.redis.pool.max-idle:100}")
    private int token_maxidle;
    //最小空闲连接数, 默认0
    @Value("${token.redis.pool.min-idle:0}")
    private int token_minidle;
    //最大连接数, 默认8个
    @Value("${token.redis.pool.max-active:1000}")
    private int token_maxActive;
    //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
    @Value("${token.redis.pool.max-wait:1000}")
    private long token_maxWait;

    //@Bean(name = "tokenRedisClusterConfiguration")
    private RedisClusterConfiguration getClusterConfiguration(String _nodes, int _timeout) {
        Map<String, Object> source = new HashMap<String, Object>();
        source.put("spring.redis.cluster.nodes", _nodes);
        source.put("spring.redis.timeout", _timeout);
        //source.put("spring.redis.cluster.max-redirects", redirects);
        return new RedisClusterConfiguration(new MapPropertySource("tokenRedisClusterConfiguration", source));
    }

    /**
     * 判断是否使用token redis配置项
     *
     * @return
     */
    private boolean isTokenConfig() {
        if (StringUtils.isEmpty(token_clusterNodes) && StringUtils.isEmpty(token_host)) {
            return false;
        }
        return true;
    }

    private JedisPoolConfig bulidPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(token_maxidle);
        config.setMinIdle(token_minidle);
        config.setMaxTotal(token_maxActive);
        config.setMaxWaitMillis(token_maxWait);
        return config;
    }

    /**
     * 配置项赋值
     */
    private void convertConfig(){
        if(!isTokenConfig()){
            token_database = database;
            token_host = host;
            token_port = port;
            token_password = password;
            token_clusterNodes = clusterNodes;
            token_timeout = timeout;
            token_maxidle = maxidle ;
            token_minidle = minidle;
            token_maxActive = maxActive;
            token_maxWait = maxWait;
        }
    }

    @Bean(name = "tokenJedisConnectionFactory")
    public JedisConnectionFactory getConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        if (StringUtils.isEmpty(token_clusterNodes)) {
            factory.setDatabase(token_database);
            factory.setHostName(token_host);
            factory.setPort(token_port);
        } else {
            factory = new JedisConnectionFactory(getClusterConfiguration(token_clusterNodes, token_timeout));
        }

        factory.setPassword(token_password);
        factory.setTimeout(token_timeout);
        factory.setPoolConfig(bulidPoolConfig());
        return factory;
    }

    @Bean(name = "tokenJedisClusterConnection")
    public JedisClusterConnection getJedisClusterConnection() {
        if (StringUtils.isEmpty(token_clusterNodes)) {
            return null;
        } else {
            return (JedisClusterConnection) getConnectionFactory().getConnection();
        }
    }

    @Bean(name = "tokenRedisTemplate")
    public RedisTemplate redisTemplateToken() {
        convertConfig();

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
        //clusterTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    /*
    @Primary
    @Bean(name="jedisConnectionFactory")
    JedisConnectionFactory jedisConnectionFactory() {
        JedisPoolConfig config = new JedisPoolConfig();
        //最大空闲连接数, 默认8个
        config.setMaxIdle(maxidle);
        //最小空闲连接数, 默认0
        config.setMinIdle(minidle);
        //最大连接数, 默认8个
        config.setMaxTotal(maxActive);
        //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        config.setMaxWaitMillis(maxWait);

        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setDatabase(database);
        factory.setHostName(host);
        factory.setPort(port);
        factory.setPassword(password);
        factory.setTimeout(timeout);
        factory.setPoolConfig(config);
        return factory;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Primary
    @Bean(name="redisTemplate")
    public RedisTemplate<String, ?> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, ?> template = new RedisTemplate();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new RedisObjectSerializer());
        return template;
    }
    */
}