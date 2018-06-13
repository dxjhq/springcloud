package com.hhly.schedule.convert;

import com.hhly.schedule.api.dto.request.TaskExecuteLogReq;
import com.hhly.schedule.api.dto.request.TaskInfoReq;
import com.hhly.schedule.api.dto.response.TaskExecuteLogResp;
import com.hhly.schedule.api.dto.response.TaskInfoResp;
import com.hhly.schedule.entity.TaskExecuteLog;
import com.hhly.schedule.entity.TaskInfo;
import org.apache.commons.lang3.StringUtils;

/**
 * @author pengchao
 * @create 2017-09-26
 * @desc
 */
public class ScheduleConvert {
    //taskInfoReq转taskInfo
    public static TaskInfo convert(TaskInfoReq taskInfoReq){
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setTaskName(taskInfoReq.getTaskName());
        taskInfo.setTaskGroup(taskInfoReq.getTaskGroup());
        taskInfo.setCronExpression(taskInfoReq.getCronExpression());
        taskInfo.setRemoteMethod(taskInfoReq.getRemoteMethod());
        taskInfo.setRemotePath(taskInfoReq.getRemotePath());
        taskInfo.setRemoteServiceName(taskInfoReq.getRemoteServiceName());
        taskInfo.setTaskStatus((byte)taskInfoReq.getTaskStatus());

        if(!StringUtils.isEmpty(taskInfoReq.getDescription())){
            taskInfo.setDescription(taskInfoReq.getDescription());
        }
        if(!StringUtils.isEmpty(taskInfoReq.getExecuteParamApi())){
            taskInfo.setExecuteParamApi(taskInfoReq.getExecuteParamApi());
        }
        if(!StringUtils.isEmpty(taskInfoReq.getExecuteParam())){
            taskInfo.setExecuteParam(taskInfoReq.getExecuteParam());
        }

        return taskInfo;
    }
    //taskInfo转taskInfoResp
    public static TaskInfoResp convert(TaskInfo taskInfo){
        TaskInfoResp taskInfoResp = new TaskInfoResp();
        taskInfoResp.setTaskName(taskInfo.getTaskName());
        taskInfoResp.setTaskGroup(taskInfo.getTaskGroup());
        taskInfoResp.setCronExpression(taskInfo.getCronExpression());
        taskInfoResp.setRemoteMethod(taskInfo.getRemoteMethod());
        taskInfoResp.setRemotePath(taskInfo.getRemotePath());
        taskInfoResp.setRemoteServiceName(taskInfo.getRemoteServiceName());
        taskInfoResp.setTaskStatus(taskInfo.getTaskStatus());

        if(!StringUtils.isEmpty(taskInfo.getDescription())){
            taskInfoResp.setDescription(taskInfo.getDescription());
        }
        if(!StringUtils.isEmpty(taskInfo.getExecuteParamApi())){
            taskInfoResp.setExecuteParamApi(taskInfo.getExecuteParamApi());
        }
        if(!StringUtils.isEmpty(taskInfo.getExecuteParam())){
            taskInfoResp.setExecuteParam(taskInfo.getExecuteParam());
        }

        return taskInfoResp;
    }
    //taskExecuteLogReq转taskExecuteLog
    public static TaskExecuteLog convert(TaskExecuteLogReq taskExecuteLogReq){
        TaskExecuteLog taskExecuteLog = new TaskExecuteLog();
        taskExecuteLog.setExecuteLog(taskExecuteLogReq.getExecuteLog());
        taskExecuteLog.setTaskId(taskExecuteLogReq.getTaskId());
        taskExecuteLog.setScheduleStartTime(taskExecuteLogReq.getScheduleStartTime());
        taskExecuteLog.setScheduleEndTime(taskExecuteLogReq.getScheduleEndTime());
        taskExecuteLog.setScheduleExecuteResult((byte)taskExecuteLogReq.getScheduleExecuteResult());
        taskExecuteLog.setTriggerWay((byte)taskExecuteLogReq.getTriggerWay());

        if(!StringUtils.isEmpty(taskExecuteLogReq.getScheduleExceptionMsg())){
            taskExecuteLog.setScheduleExceptionMsg(taskExecuteLogReq.getScheduleExceptionMsg());
        }
        if(!StringUtils.isEmpty(taskExecuteLogReq.getExecuteLog())){
            taskExecuteLog.setScheduleExceptionMsg(taskExecuteLogReq.getExecuteLog());
        }
        return taskExecuteLog;
    }
    //taskExecuteLog转taskExecuteLogResp
    public static TaskExecuteLogResp convert(TaskExecuteLog taskExecuteLog){
        TaskExecuteLogResp taskExecuteLogResp = new TaskExecuteLogResp();
        taskExecuteLogResp.setExecuteLog(taskExecuteLog.getExecuteLog());
        taskExecuteLogResp.setTaskId(taskExecuteLog.getTaskId());
        taskExecuteLogResp.setScheduleStartTime(taskExecuteLog.getScheduleStartTime());
        taskExecuteLogResp.setScheduleEndTime(taskExecuteLog.getScheduleEndTime());
        taskExecuteLogResp.setScheduleExecuteResult(taskExecuteLog.getScheduleExecuteResult());
        taskExecuteLogResp.setTriggerWay(taskExecuteLog.getTriggerWay());

        if(!StringUtils.isEmpty(taskExecuteLog.getScheduleExceptionMsg())){
            taskExecuteLogResp.setScheduleExceptionMsg(taskExecuteLog.getScheduleExceptionMsg());
        }
        if(!StringUtils.isEmpty(taskExecuteLog.getExecuteLog())){
            taskExecuteLogResp.setScheduleExceptionMsg(taskExecuteLog.getExecuteLog());
        }
        return taskExecuteLogResp;
    }
}