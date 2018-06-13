package com.hhly.schedule.api.enums;

/**
 * @author pengchao
 * @create 2017-09-19
 * @desc
 */
public enum TriggerWay {
    MANUAL(1, "手动"),
    AUTO(0, "自动");

    private int code;
    private String triggerWay;

    private TriggerWay(int code, String triggerWay) {
        this.code = code;
        this.triggerWay = triggerWay;
    }

    public String getTriggerWay() {
        return triggerWay;
    }

    public void setTriggerWay(String triggerWay) {
        this.triggerWay = triggerWay;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
