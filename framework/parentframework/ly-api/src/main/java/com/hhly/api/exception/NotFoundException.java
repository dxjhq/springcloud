package com.hhly.api.exception;

/**
 * 没有找到服务异常类
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("not found");
    }
    public NotFoundException(String msg) {
        super(msg);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
