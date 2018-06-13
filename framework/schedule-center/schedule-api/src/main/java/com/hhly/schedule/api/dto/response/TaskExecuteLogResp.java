package com.hhly.schedule.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author pengchao
 * @create 2017-09-19
 * @desc
 */
@Setter
@Getter
public class TaskExecuteLogResp {
    private int id;
    /**
     * 字段名称：任务主键
     *
     * 数据库字段信息:task_id Integer(19)
     */
    private int taskId;

    /**
     * 不存表，前端展示用
     */
    private String taskName;
    /**
     * 不存表，前端展示用
     */
    private String taskGroup;

    /**
     * 字段名称：执行日志
     *
     * 数据库字段信息:execute_log VARCHAR(5000)
     */
    private String executeLog;

    /**
     * 字段名称：异常信息
     *
     * 数据库字段信息:exception_msg VARCHAR(5000)
     */
    private String scheduleExceptionMsg;

    /**
     * 字段名称：调度开始时间
     *
     * 数据库字段信息:schedule_start_time DATETIME(19)
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
    private Timestamp scheduleStartTime;

    /**
     * 字段名称：调度结束时间
     *
     * 数据库字段信息:schedule_end_time DATETIME(19)
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
    private Timestamp scheduleEndTime;


    /**
     * 字段名称：执行结果
     *
     * 数据库字段信息:result smallint
     */
    private int scheduleExecuteResult;

    /**
     * 字段名称：任务触发方式
     *
     * 数据库字段信息:trigger_way smallint
     */
    private int triggerWay;
}

