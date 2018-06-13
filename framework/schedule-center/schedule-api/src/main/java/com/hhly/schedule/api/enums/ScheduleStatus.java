package com.hhly.schedule.api.enums;

/**
 * @author pengchao
 * @create 2017-09-19
 * @desc
 */
public enum  ScheduleStatus {
    SUCCESS(0, "成功"),
    FAIL(1, "失败"),
    IGNORE(2, "忽略");   //服务不存在或者服务调不通

    private int code;
    private String status;

    private ScheduleStatus(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
