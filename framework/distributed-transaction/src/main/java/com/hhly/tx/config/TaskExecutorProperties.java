package com.hhly.tx.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wangxianchen
 * @create 2017-10-23
 * @desc 线程池配置属性
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "txTaskExecutor")
public class TaskExecutorProperties {

    //核心线程数
    private int corePoolSize = 3;

    //最大线程数
    private int maxPoolSize = 10;

    //队列最大长度
    private int queueCapacity = 50;

    //线程池维护线程所允许的空闲时间,单位:秒
    private int keepAliveSeconds = 300;

}
