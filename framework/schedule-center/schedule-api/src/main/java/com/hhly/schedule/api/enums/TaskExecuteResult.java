package com.hhly.schedule.api.enums;

/**
 * @author pengchao
 * @create 2017-09-19
 * @desc
 */
public enum TaskExecuteResult {
    SUCCESS(0, "成功"),
    FAIL(1, "失败");

    private int code;
    private String result;

    private TaskExecuteResult(int code, String result) {
        this.code = code;
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
