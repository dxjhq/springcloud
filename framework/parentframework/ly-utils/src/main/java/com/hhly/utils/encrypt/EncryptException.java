package com.hhly.utils.encrypt;

/**
 * Created by wangxianchen on 2017/8/5.
 * 加密解密出现的异常
 */
public class EncryptException extends RuntimeException {

    public EncryptException() {
        super();
    }

    /**
     *
     * @param message
     */
    public EncryptException(String message) {
        super(message);
    }

    /**
     *
     * @param cause
     */
    public EncryptException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public EncryptException(String message, Throwable cause) {
        super(message, cause);
    }
}
