package com.hhly.api.exception;

import com.hhly.api.enums.ErrorCodeEnum;
import com.hhly.api.enums.IEnum;

/**
* @author wangxianchen
* @create 2017-09-01
* @desc 自定义异常类
*/
public class ValidationException extends RuntimeException {

    private IEnum errorCodeEnum;

    private Object param;

    public ValidationException() {
        super();
        errorCodeEnum = ErrorCodeEnum.FAIL;
    }
    public ValidationException(Object param) {
        super();
        this.param = param;
        errorCodeEnum = ErrorCodeEnum.FAIL;
    }

    public ValidationException(IEnum errorCodeEnum) {
        super(errorCodeEnum.message());
        this.errorCodeEnum = errorCodeEnum;
    }

    public ValidationException(IEnum errorCodeEnum, Object param) {
        super(errorCodeEnum.message());
        this.errorCodeEnum = errorCodeEnum;
        this.param = param;
    }


    public ValidationException(String message) {
        super(message);
        ErrorCodeEnum errorEnum = ErrorCodeEnum.FAIL;
        errorEnum.setMessage(message);
        errorCodeEnum = errorEnum;
    }


    public ValidationException(Throwable cause) {
        super(cause);
    }


    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public IEnum getErrorCodeEnum() {
        return errorCodeEnum;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "ValidationException{" +
                "errorCode=" + errorCodeEnum.code() +
                "errorMsg=" + errorCodeEnum.message() +
                ", param=" + (param == null ? null : param.toString()) +
                '}';
    }
}
