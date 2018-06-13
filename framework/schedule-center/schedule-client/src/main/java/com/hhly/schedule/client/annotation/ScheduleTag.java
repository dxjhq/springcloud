package com.hhly.schedule.client.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 此注解用于标记向调度中心注册任务
 *
 * @author pengchao
 * @create 2017-09-19
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ScheduleTag {
}
