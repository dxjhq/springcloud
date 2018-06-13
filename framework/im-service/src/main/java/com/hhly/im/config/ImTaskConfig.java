package com.hhly.im.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wangxianchen
 * @create 2017-10-23
 * @desc 配置线程池
 */
@Configuration
public class ImTaskConfig {

    @Bean(name="ImThreadPoolTaskExecutor")
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
