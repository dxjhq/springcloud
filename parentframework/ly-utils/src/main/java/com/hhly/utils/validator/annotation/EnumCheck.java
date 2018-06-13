package com.hhly.utils.validator.annotation;

import com.hhly.utils.validator.core.EnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
* @author wangxianchen
* @create 2017-08-08
* @desc 针对枚举类型的验证器,对空不进行验证
*/
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER }) //应用范围
@Retention(RUNTIME) //运行时机
@Constraint(validatedBy = {EnumValidator.class}) //和注解关联的验证器，实验验证的功能
public @interface EnumCheck {

    String message() default "没有找到匹配项"; //验证输出消息

    Class<?>[] groups() default { }; //约束所属组别

    Class<? extends Payload>[] payload() default {};//负载

    Class<?> value();


}