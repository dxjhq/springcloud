package com.hhly.schedule.api.enums;

/**
 * @author pengchao
 * @create 2017-09-19
 * @desc
 */
public enum TaskStatus {
    ENABLED(0, "启用"),
    DISABLED(1, "禁用"),
    END(2, "结束");

    private int code;
    private String status;

    private TaskStatus(int code, String status) {
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
