package com.hhly.schedule.api.enums;

/**
 * @author pengchao
 * @create 2017-09-19
 * @desc
 */
public enum ScheduleMessage{
    JOB_EXECUTING("远程任务还在执行中，忽略此次调度"),
    JOB_NOT_FOUND("远程任务执行器未找到，禁用此任务"),
    SERVICE_UNAVAILABLE("远程任务服务熔断，服务不可用"),
    HTTP("第三方服务，只发起Http请求");

    private String message;

    private ScheduleMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
