package com.hhly.utils.validator.core;

import com.hhly.utils.validator.annotation.EnumCheck;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
* @author wangxianchen
* @create 2017-08-08
* @desc 验证是否可以转换为相应的枚举类型，为空则不校验
*/
public class EnumValidator implements ConstraintValidator<EnumCheck,String> {

    private Class<Enum> enumClass;

    @Override
    public void initialize(EnumCheck constraintAnnotation) {
       enumClass = (Class<Enum>) constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(StringUtils.isNotEmpty(value)){
            Enum[] enums = enumClass.getEnumConstants();
            for(Enum e:enums){
                if(value.equals(e.name())){
                    return true;
                }
            }
            return false;
        }
        return true;
    }
}
