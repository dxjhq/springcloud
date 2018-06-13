package com.hhly.schedule.client.annotation;

import org.springframework.stereotype.Component;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * client通过此注解向调度中心注册任务
 *
 * @author pengchao
 * @create 2017-09-19
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ScheduleHandlerTag {
    //表达式,默认10分钟一次
    String cronExpression() default "0 0/10 * * * ?";

    //执行任务的API接口地址
    String scheduleApi() default "";

    //执行任务的API接口访问方式
    String scheduleMethod() default "GET";

    //静态执行参数 json
    //{"key1":"value","key2":"value2","key3":"value3"}
    String paramsJson() default "";

    //用于动态获取执行参数
    String paramsApi() default "";

    //描述
    String description() default "";
}