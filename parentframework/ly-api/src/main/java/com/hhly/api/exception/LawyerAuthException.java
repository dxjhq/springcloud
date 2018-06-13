package com.hhly.api.exception;

/** 律师未认证异常 */
public class LawyerAuthException extends RuntimeException {

    public LawyerAuthException() {super("律师未认证, 无法进行此操作");}

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
