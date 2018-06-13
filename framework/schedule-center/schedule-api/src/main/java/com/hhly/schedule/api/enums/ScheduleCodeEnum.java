package com.hhly.schedule.api.enums;

import com.hhly.common.dto.ErrorCodeEnum;

/**
 * @author pengchao
 * @create 2017-09-26
 * @desc
 */
public enum ScheduleCodeEnum {
    SCHEDULE_ERROR(3000, "%s");

    private int code;
    private String message;
    private String format;

    private ScheduleCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
        this.format = message;
    }

    public ScheduleCodeEnum format(Object... msgArgs) {
        this.message = String.format(this.format, msgArgs);
        return this;
    }

    public String toString() {
        return Integer.toString(this.getCode());
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
