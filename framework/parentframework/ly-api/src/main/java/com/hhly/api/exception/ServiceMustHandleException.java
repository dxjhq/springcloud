package com.hhly.api.exception;

/**
 * 业务异常. 一般要在 Service 层抛出, 在 controller 中强制处理, 主要是为了将异常信息返回到前台. 此强制异常不会将事务回滚!
 */
public class ServiceMustHandleException extends Exception {

    public ServiceMustHandleException() {super();}
    public ServiceMustHandleException(String msg) {super(msg);}
    public ServiceMustHandleException(Throwable cause) {
        super(cause);
    }
    public ServiceMustHandleException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
