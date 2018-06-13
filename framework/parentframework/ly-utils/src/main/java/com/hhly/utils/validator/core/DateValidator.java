package com.hhly.utils.validator.core;

import com.hhly.utils.validator.annotation.DateCheck;
import com.hhly.utils.date.DateUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
* @author wangxianchen
* @create 2017-08-08
* @desc 日期校验器
*/
public class DateValidator implements ConstraintValidator<DateCheck,String> {

    private String formatPattern;

    @Override
    public void initialize(DateCheck constraintAnnotation) {
        this.formatPattern = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(StringUtils.isEmpty(value)){
            return true;
        }else{
            if(DateUtil.convertDate(value, formatPattern) != null){
                return true;
            }
        }
        return false;
    }
}
