package com.hhly.schedule.api.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author pengchao
 * @create 2017-09-19
 * @desc
 */
@Getter
@Setter
public class TaskInfoResp {
    /**
     * 字段名称：任务名称
     *
     * 数据库字段信息:task_name VARCHAR(50)
     */
    private String taskName;

    /**
     * 字段名称：任务组名
     *
     * 数据库字段信息:task_group VARCHAR(50)
     */
    private String taskGroup;

    /**
     * 字段名称：服务名称
     *
     * 数据库字段信息:remote_service_name VARCHAR(100)
     */
    private String remoteServiceName;

    /**
     * 字段名称：服务路径
     *
     * 数据库字段信息:remote_path VARCHAR(100)
     */
    private String remotePath;

    /**
     * 字段名称：服务路径
     *
     * 数据库字段信息:remote_path VARCHAR(100)
     */
    private String remoteMethod;

    /**
     * 字段名称：任务状态
     *
     * 数据库字段信息:task_status VARCHAR(50)
     */
    private int taskStatus;

    /**
     * 字段名称：表达式
     *
     * 数据库字段信息:cron_expression VARCHAR(50)
     */
    private String cronExpression;

    /**
     * 字段名称：执行参数
     *
     * 数据库字段信息:execute_param VARCHAR(1000)
     */
    private String executeParam;

    /**
     * 字段名称：获取执行参数的接口
     *
     * 数据库字段信息:execute_param_api VARCHAR(1000)
     */
    private String executeParamApi;

    /**
     * 字段名称：任务描述
     *
     * 数据库字段信息:description VARCHAR(500)
     */
    private String description;
}
