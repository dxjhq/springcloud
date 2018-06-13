package com.hhly.tx.enums;


import com.hhly.api.enums.IEnum;

/**
 * @author wangxianchen
 * @create 2017-11-16
 * @desc 状态枚举
 */
public enum  TxEnum implements IEnum {

    WAIT_FOR_PERFORMED(0,"待执行"),

    SUCCESS(1,"成功"),

    FAIL(2,"失败");

    private int code;
    private String message;

    TxEnum(int code,String message){
        this.code = code;
        this.message = message;
    }
    @Override
    public int code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }
}
