package com.hhly.im.exception;

/**
 * @author wangxianchen
 * @create 2017-10-24
 * @desc 未超过间隔时间异常
 */
public class NotExceedingIntervalTime extends RuntimeException {

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public NotExceedingIntervalTime(String msg) {
        super(msg);
    }

    public NotExceedingIntervalTime() {
        super();
    }
}
