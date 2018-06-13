package com.hhly.tx.componet;

import com.hhly.tx.config.TaskExecutorProperties;
import com.hhly.tx.service.MessageReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wangxianchen
 * @create 2017-11-17
 * @desc 消息推送配置
 */
public class PublishConfig {

    public static final String _TOPIC = "_txMsgChat";

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Value("${spring.application.name}")
    private String appServiceName;

    @Bean
    public RedisMessageListenerContainer container(MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(listenerAdapter, new ChannelTopic(appServiceName+_TOPIC));

        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(MessageReceiverService messageReceiverService) {
        return new MessageListenerAdapter(messageReceiverService);
    }

    @Bean
    public MessageReceiverService messageReceiverService() {
        return new MessageReceiverService();
    }

    @Bean(name="txThreadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(@Autowired(required = false) TaskExecutorProperties taskExecutorProperties){
        if(taskExecutorProperties == null){
            taskExecutorProperties = new TaskExecutorProperties();
        }
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(taskExecutorProperties.getCorePoolSize());
        taskExecutor.setMaxPoolSize(taskExecutorProperties.getMaxPoolSize());
        taskExecutor.setQueueCapacity(taskExecutorProperties.getQueueCapacity());
        taskExecutor.setKeepAliveSeconds(taskExecutorProperties.getKeepAliveSeconds());
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return taskExecutor;
    }
}
