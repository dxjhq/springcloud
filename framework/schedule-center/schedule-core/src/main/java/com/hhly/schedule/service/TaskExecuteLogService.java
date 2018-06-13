package com.hhly.schedule.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.hhly.common.dto.ErrorCodeEnum;
import com.hhly.common.dto.ResultObject;
import com.hhly.common.util.UserUtil;
import com.hhly.schedule.api.dto.request.TaskExecuteLogQueryReq;
import com.hhly.schedule.api.dto.request.TaskExecuteLogReq;
import com.hhly.schedule.api.dto.request.TaskInfoQueryReq;
import com.hhly.schedule.api.dto.request.TaskInfoReq;
import com.hhly.schedule.api.dto.response.TaskExecuteLogResp;
import com.hhly.schedule.api.dto.response.TaskInfoResp;
import com.hhly.schedule.convert.ScheduleConvert;
import com.hhly.schedule.dao.TaskExecuteLogDao;
import com.hhly.schedule.entity.TaskExecuteLog;
import com.hhly.schedule.entity.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * 业务类
 *
 * @author ：pengchao
 * @createTime ：2017/09/25
 */
@Service("taskExecuteLogService")
public class TaskExecuteLogService {

    @Autowired
    private TaskExecuteLogDao taskExecuteLogDao;

    public TaskExecuteLog getLastExecuteLogByTask(Long taskId) {
        return taskExecuteLogDao.getLastExecuteLogByTask(taskId);
    }

    public TaskExecuteLog getLastSuccessLogByTask(Long taskId) {
        return taskExecuteLogDao.getLastSuccessLogByTask(taskId);
    }

    public ResultObject get(int id) {
        TaskExecuteLog taskExecuteLog = taskExecuteLogDao.selectByPrimaryKey(id);
        if (taskExecuteLog == null) {
            return new ResultObject(ErrorCodeEnum.QUERY_DATA_ERROR);
        } else {
            ResultObject result = new ResultObject();
            result.setData(ScheduleConvert.convert(taskExecuteLog));
            return result;
        }
    }
    public ResultObject add(TaskExecuteLogReq taskExecuteLogReq){
        TaskExecuteLog taskExecuteLog = ScheduleConvert.convert(taskExecuteLogReq);
        return add(taskExecuteLogReq);
    }
    public ResultObject add(TaskExecuteLog taskExecuteLog){
        boolean isSuccess = taskExecuteLogDao.insertSelective(taskExecuteLog);
        ResultObject result = isSuccess ?
                new ResultObject() :
                new ResultObject(ErrorCodeEnum.FAIL);
        return result;
    }
    public ResultObject update(TaskExecuteLogReq taskExecuteLogReq) {
        TaskExecuteLog taskExecuteLog_old = taskExecuteLogDao.selectByPrimaryKey(taskExecuteLogReq.getId());
        if(taskExecuteLog_old == null){
            return new ResultObject(ErrorCodeEnum.UPDATE_RECORD_NOT_EXIST_ERROR);
        }
        TaskExecuteLog taskExecuteLog = ScheduleConvert.convert(taskExecuteLogReq);
        boolean isSuccess = taskExecuteLogDao.updateByPrimaryKeySelective(taskExecuteLog);
        ResultObject result = isSuccess ?
                new ResultObject() :
                new ResultObject(ErrorCodeEnum.FAIL);
        return result;
    }
    public ResultObject delete(int id) {
        TaskExecuteLog taskExecuteLog_old = taskExecuteLogDao.selectByPrimaryKey(id);
        if (taskExecuteLog_old == null) {
            return new ResultObject(ErrorCodeEnum.PARAM_EXCEPTION.format("数据不匹配"));
        }else{
            Boolean isSuccess = taskExecuteLogDao.deleteByPrimaryKey(id);
            ResultObject result = isSuccess ?
                    new ResultObject() :
                    new ResultObject(ErrorCodeEnum.FAIL);
            return result;
        }
    }

    /**
     * @desc 获取分页列表
     */
    public ResultObject page(TaskExecuteLogQueryReq taskExecuteLogQueryReq, PageBounds pageBounds) {
        ResultObject result = new ResultObject();
        List<TaskExecuteLog> taskExecuteLogList = taskExecuteLogDao.selectWithPage(taskExecuteLogQueryReq, pageBounds);
        List<TaskExecuteLogResp> taskExecuteLogRespList= new ArrayList<TaskExecuteLogResp>();
        for (TaskExecuteLog task :
                taskExecuteLogList) {
            taskExecuteLogRespList.add(ScheduleConvert.convert(task));
        }
        result.setData(taskExecuteLogRespList);
        return result;
    }

    public TaskExecuteLog buildEntity(int taskId,int executeResult,int triggerWay,String exceptionMsg,String executeLog){
        TaskExecuteLog taskExecuteLog = new TaskExecuteLog();
        Timestamp scheduleTime = new Timestamp(System.currentTimeMillis());
        taskExecuteLog.setTaskId(taskId);
        taskExecuteLog.setScheduleStartTime(scheduleTime);
        taskExecuteLog.setScheduleEndTime(scheduleTime);
        taskExecuteLog.setScheduleExceptionMsg(exceptionMsg);
        taskExecuteLog.setExecuteLog(executeLog);
        taskExecuteLog.setScheduleExecuteResult((byte)executeResult);
        taskExecuteLog.setTriggerWay((byte)triggerWay);
        return taskExecuteLog;
    }
}