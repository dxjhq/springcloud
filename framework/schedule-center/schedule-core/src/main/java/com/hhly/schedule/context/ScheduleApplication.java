package com.hhly.schedule.context;

import com.hhly.common.spring.BaseConfiguration;
import com.hhly.common.spring.MybatisConfig;
import com.hhly.common.util.ApplicationContextUtil;
import com.hhly.schedule.config.QuartzConfig;
import com.hhly.schedule.config.WebConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author pengchao
 * @create 2017-09-19
 * @desc
 */
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan({"com.hhly.schedule.*"})
@Import({BaseConfiguration.class,
        MybatisConfig.class,
        WebConfiguration.class,
        QuartzConfig.class})
public class ScheduleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScheduleApplication.class, args);
    }

}