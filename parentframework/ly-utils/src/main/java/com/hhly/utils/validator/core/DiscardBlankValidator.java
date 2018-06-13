package com.hhly.utils.validator.core;

import com.hhly.utils.validator.annotation.DiscardBlank;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

/**
* @author wangxianchen
* @create 2017-08-02
* @desc 对成员属性的值进行重设,针对空和空格的一律设为null
*/
public class DiscardBlankValidator implements ConstraintValidator<DiscardBlank,Object> {

    @Override
    public void initialize(DiscardBlank constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field:fields){
            String val;
            if(field.getType() == String.class){
                try {
                    field.setAccessible(true);
                    val = field.get(obj) == null ? null : field.get(obj).toString().trim();
                    val = val == null || val.length() == 0 ? null : val;
                    field.set(obj,val);
                } catch (IllegalAccessException e) {
                    //throw new IllegalAccessException("非空检查出现异常");
                    //LogUtil.ROOT_LOG.error("非空检查出现异常",e);
                    return false;
                }
            }
        }
        return true;
    }
}
