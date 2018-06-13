package com.hhly.utils;

import java.util.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

/**
* @author wangxianchen
* @create 2017-08-28
* @desc 字段校验工具
*/

public class ValidatorUtil {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> String validate(T t) {
        String title = null;
        if(t == null){
            return "参数不能为空";
        }
        Set<ConstraintViolation<T>> set = validator.validate(t,Default.class);
        if(set != null && set.size() >0 ){
            StringBuffer sb = new StringBuffer();
            for (ConstraintViolation<T> cv : set) {
                sb.append(cv.getPropertyPath().toString());
                sb.append("=");
                sb.append(cv.getMessage());
                sb.append(",");
            }
            if(sb.length()!=0){
                title = sb.toString().substring(0,sb.length()-1);
            }
        }
        return title;
    }
}
