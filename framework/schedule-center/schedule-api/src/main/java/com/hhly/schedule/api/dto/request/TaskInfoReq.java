package com.hhly.schedule.api.dto.request;

import com.hhly.common.annotation.ContainCheck;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * @author pengchao
 * @create 2017-09-19
 * @desc
 */
@Setter
@Getter
public class TaskInfoReq {

    private int id;
    /**
     * 字段名称：任务名称
     *
     * 数据库字段信息:task_name VARCHAR(50)
     */
    @NotEmpty(message="TaskName不能为空")
    @Size(max = 200,message = "TaskName内容长度不能超过200个字符")
    private String taskName;

    /**
     * 字段名称：任务组名
     *
     * 数据库字段信息:task_group VARCHAR(50)
     */
    @NotEmpty(message="TaskGroup不能为空")
    @Size(max = 100,message = "TaskGroup内容长度不能超过100个字符")
    private String taskGroup;

    /**
     * 字段名称：服务名称
     *
     * 数据库字段信息:remote_service_name VARCHAR(100)
     */
    @NotEmpty(message="remoteServiceName不能为空")
    @Size(max = 50,message = "remoteServiceName内容长度不能超过50个字符")
    private String remoteServiceName;

    /**
     * 字段名称：服务路径
     *
     * 数据库字段信息:remote_path VARCHAR(100)
     */
    @NotEmpty(message="remotePath不能为空")
    @Size(max = 100,message = "remotePath内容长度不能超过100个字符")
    private String remotePath;

    /**
     * 字段名称：服务路径
     *
     * 数据库字段信息:remote_path VARCHAR(100)
     */
    @NotEmpty(message="remoteMethod不能为空")
    @ContainCheck(value = "POST,GET,PUT",message = "remoteMethod不正确")
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
    @NotEmpty(message="cronExpression不能为空")
    @Size(max = 50,message = "cronExpression内容长度不能超过50个字符")
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