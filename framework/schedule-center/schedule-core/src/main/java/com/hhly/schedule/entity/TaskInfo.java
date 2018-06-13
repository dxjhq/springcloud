package com.hhly.schedule.entity;

import com.hhly.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author ：pengchao
 * @createTime ：2017/09/25
 */
@Getter
@Setter
@ToString
public class TaskInfo extends BaseEntity {

    // 任务名
    private String taskName;

    // 任务组
    private String taskGroup;

    // 执行任务的服务名
    private String remoteServiceName;

    // 执行任务的API
    private String remotePath;

    // 执行任务的API的调用方式 GET POST 等
    private String remoteMethod;

    // (0:禁用 1:启用 2:结束)  默认：0
    private Byte taskStatus;

    // (0:待执行 1:执行中)  默认：0
    private Byte executeStatus;

    // 执行周期表达式
    private String cronExpression;

    // 执行参数
    private String executeParam;

    // 动态执行参数获取接口
    private String executeParamApi;

    private String description;

    private Date preExecuteTime;

}