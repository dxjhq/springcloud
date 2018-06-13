package com.hhly.schedule.entity;

import com.hhly.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author ：pengchao
 * @createTime ：2017/09/25
 */
@Getter
@Setter
@ToString
public class TaskExecuteLog extends BaseEntity {


    // 对应的任务ID  默认：0
    private Integer taskId;

    // 开始时间  默认：CURRENT_TIMESTAMP
    private Timestamp scheduleStartTime;

    // 开始时间  默认：CURRENT_TIMESTAMP
    private Timestamp scheduleEndTime;

    // 错误信息
    private String scheduleExceptionMsg;

    // (0:成功 1:失败)  默认：0
    private Byte scheduleExecuteResult;

    // 执行日志
    private String executeLog;

    // (0:自动 1:手动)  默认：0
    private Byte triggerWay;
}