package com.hhly.web.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by jasonchai on 2017/6/8.
 */
public class ApplicationConstant {

    @Value("${spring.cloud.stream.kafka.binder.zkNodes:}")
    public String zkAddress;

    @Value("${spring.application.name}")
    public String applicationName;

    @Value("${spring.application.index:0}")
    public int applicationIndex;

}
