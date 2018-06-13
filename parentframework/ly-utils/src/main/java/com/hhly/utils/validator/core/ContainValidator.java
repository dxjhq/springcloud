package com.hhly.utils.validator.core;

import com.hhly.utils.validator.annotation.ContainCheck;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
* @author wangxianchen
* @create 2017-08-08
* @desc 字符校验器 不对空字符进行校验,默认返回true
*/
public class ContainValidator implements ConstraintValidator<ContainCheck,Object> {

    private String[] arr;

    /**
     * STRING 字符串 包含
     * NUMBER 数字  范围
     */
    private String type;

    @Override
    public void initialize(ContainCheck constraintAnnotation) {
        arr = constraintAnnotation.value().split(",");
        type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(value == null){
            return true;
        }
        if("STRING".equals(type)){
            for(String str : arr){
                if(str.equals(value.toString())){
                    return true;
                }
            }
        }else if("NUMBER".equals(type)){
            try {
                long val = Long.valueOf(value.toString());
                long s1 = Long.valueOf(arr[0]);
                long s2 = Long.valueOf(arr[1]);
                if(s1 <= val && val <= s2){
                    return true;
                }
            } catch (NumberFormatException e) {
                throw new NumberFormatException("\"字符转数字出错 str=\"+value");
            }
        }
        return false;
    }
}
