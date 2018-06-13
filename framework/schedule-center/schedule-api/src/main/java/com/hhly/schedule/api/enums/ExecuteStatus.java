package com.hhly.schedule.api.enums;

/**
 * @author pengchao
 * @create 2017-09-29
 * @desc
 */
public enum ExecuteStatus {
    WAIT(0, "待执行"),
    RUNNING(1, "执行中");

    private int code;
    private String status;

    private ExecuteStatus(int code, String status) {
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
