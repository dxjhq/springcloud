package com.hhly.api.exception;

/**
 * 没有访问权限
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
        super("没有权限");
    }
    public ForbiddenException(String msg) {
        super(msg);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
