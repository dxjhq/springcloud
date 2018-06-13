package com.hhly.utils.validator.core;

import com.hhly.utils.validator.annotation.ContainCheck;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author wangxianchen
 * @create 2017-09-08
 * @desc 校验测试器
 */
public class ValidatorTester {

    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    public static <T> List<String> validate(T t,boolean isPrint) {
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);

        List<String> messageList = new ArrayList<>();
        for (ConstraintViolation<T> constraintViolation : constraintViolations) {
            if(isPrint){
                System.out.println(constraintViolation.getMessage());
            }
            messageList.add(constraintViolation.getMessage());
        }
        return messageList;
    }



    @NotNull(message = "是否可以接单字符不能为空")
    @ContainCheck(value = "1,2",message = "是否可以接单字符格式错误",type="NUMBER")
    private Byte isWork;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",message = "日期格式不对")
    private String date;

    public static void main(String[] args) {
        ValidatorTester test = new ValidatorTester();
        test.isWork = 2;
        //test.date = "0010-12-22";
        List<String> list = ValidatorTester.validate(test, true);
        for(String s:list){
            System.out.println(s);
        }
    }

}
