package com.hhly.api.exception;

/**
 * 用户未登录的统一处理.
 */
public class NotLoginException extends RuntimeException {

    public NotLoginException() {
        super("请先登录.");
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
