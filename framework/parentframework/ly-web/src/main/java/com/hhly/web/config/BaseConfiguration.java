package com.hhly.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by jasonchai on 2017/6/8.
 */
@EnableHystrix
@ComponentScan({"com.hhly.**.component","com.hhly.**.dao","com.hhly.**.service", "com.hhly.**.web","com.hhly.**.api","com.hhly.**.client"})
//@Import({ApplicationContextUtil.class}) //工具类中自动装配
public class BaseConfiguration {

    private static Logger logger = LoggerFactory.getLogger(BaseConfiguration.class);

    @Value("${spring.application.name:}")
    String appName;
    @Value("${server.port:}")
    String port;

    @Bean
    public ApplicationConstant applicationConstant() {
        return new ApplicationConstant();
    }

    @Bean
    public ApplicationContextHolder applicationContextHolder() {
        return ApplicationContextHolder.getInstance();
    }

    @Bean
    public CommandLineRunner runner(){
        return new CommandLineRunner() {
            public void run(String... args){
                try {
                    InetAddress ia = InetAddress.getLocalHost();//获取本地IP对象
                    logger.warn("visit link:  http://"+ia.getHostAddress()+":"+port+"/status");
                } catch (UnknownHostException e) {
                    logger.error("获取本机IP失败",e);
                }
            }
        };
    }
}