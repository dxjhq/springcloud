package com.hhly.tx.annotation;

import com.hhly.tx.service.AbstractRollbackService;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author wangxianchen
 * @create 2017-11-16
 * @desc 分布式事务标记
 */

@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface TxRollbackHandle {

    //回滚服务ClassType
    Class<? extends AbstractRollbackService> bean() default AbstractRollbackService.class;

    //回滚服务对应方法
    String method() default "";
}
