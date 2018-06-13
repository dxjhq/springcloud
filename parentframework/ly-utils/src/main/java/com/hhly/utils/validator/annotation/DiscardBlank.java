package com.hhly.utils.validator.annotation;

import com.hhly.utils.validator.core.DiscardBlankValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
* @author wangxianchen
* @create 2017-08-10
* @desc 对成员属性的值进行重设,针对空和空格的一律设为null
*/
@Target({ TYPE }) //应用范围
@Retention(RUNTIME) //运行时机
@Constraint(validatedBy = {DiscardBlankValidator.class}) //和注解关联的验证器，实验验证的功能
public @interface DiscardBlank {

    String message() default "重设属性值时出错"; //验证输出消息

    Class<?>[] groups() default { }; //约束所属组别

    Class<? extends Payload>[] payload() default {};//负载

}