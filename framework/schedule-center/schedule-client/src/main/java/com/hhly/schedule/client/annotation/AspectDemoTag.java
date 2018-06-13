package com.hhly.schedule.client.annotation;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author pengchao
 * @create 2017-09-30
 * @desc
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface AspectDemoTag {
    //表达式
    String name() default "";
}
