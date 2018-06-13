package com.hhly.utils.validator.annotation;

import com.hhly.utils.validator.core.DateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
* @author wangxianchen
* @create 2017-08-10
* @desc 针对日期的验证器,不对空进行验证
*/
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER }) //应用范围
@Retention(RUNTIME) //运行时机
@Constraint(validatedBy = {DateValidator.class}) //和注解关联的验证器，实验验证的功能
public @interface DateCheck {

    String message() default "错误的日期格式"; //验证输出消息

    Class<?>[] groups() default { }; //约束所属组别

    Class<? extends Payload>[] payload() default {};//负载

    String value();


}