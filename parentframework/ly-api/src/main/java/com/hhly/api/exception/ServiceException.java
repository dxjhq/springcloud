package com.hhly.api.exception;

import com.hhly.api.enums.ErrorCodeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 业务异常. 与 ServiceMustHandleException 不同, 此异常会跳到一个指定的错误页面而丢失原先的用户操作
 */
@Getter
@Setter
public class ServiceException extends RuntimeException {

    private ErrorCodeEnum errorCodeEnum;
    public ServiceException() {super("业务异常");}
    public ServiceException(String msg) {super(msg);}

    public ServiceException(ErrorCodeEnum errorCodeEnum){
        this.errorCodeEnum=errorCodeEnum;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
