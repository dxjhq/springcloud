package com.hhly.utils.validator.annotation;

import com.hhly.utils.validator.core.ContainValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
* @author wangxianchen
* @create 2017-08-08
* @desc 判断字符是否在指定字符数组内,不对空字符进行校验,默认返回true
*/
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER }) //应用范围
@Retention(RUNTIME) //运行时机
@Constraint(validatedBy = {ContainValidator.class}) //和注解关联的验证器，实验验证的功能
public @interface ContainCheck {

    String message() default "不包含该字符串"; //验证输出消息

    Class<?>[] groups() default { }; //约束所属组别

    Class<? extends Payload>[] payload() default {};//负载

    String value();

    String type() default "STRING";


}