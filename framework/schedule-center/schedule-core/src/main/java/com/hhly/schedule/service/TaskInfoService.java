package com.hhly.schedule.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.hhly.common.dto.ErrorCodeEnum;
import com.hhly.common.dto.ResultObject;
import com.hhly.common.util.UserUtil;
import com.hhly.schedule.api.dto.request.TaskInfoQueryReq;
import com.hhly.schedule.api.dto.request.TaskInfoReq;
import com.hhly.schedule.api.dto.response.TaskInfoResp;
import com.hhly.schedule.api.enums.TaskStatus;
import com.hhly.schedule.convert.ScheduleConvert;
import com.hhly.schedule.dao.TaskInfoDao;
import com.hhly.schedule.schedule.ScheduleManager;
import com.hhly.schedule.entity.TaskInfo;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务类
 *
 * @author ：pengchao
 * @createTime ：2017/09/25
 */
@Service("taskInfoService")
public class TaskInfoService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private TaskInfoDao taskInfoDao;

    public ResultObject get(int id) {
        TaskInfo taskInfo = taskInfoDao.selectByPrimaryKey(id);
        if (taskInfo == null) {
            return new ResultObject(ErrorCodeEnum.QUERY_DATA_ERROR);
        } else {
            ResultObject result = new ResultObject();
            result.setData(ScheduleConvert.convert(taskInfo));
            return result;
        }
    }
    public ResultObject register(TaskInfoReq taskInfoReq){
        TaskInfo taskInfo = ScheduleConvert.convert(taskInfoReq);
        TaskInfo taskInfoOld = selectUniqueness(taskInfoReq.getTaskName());

        boolean isSuccess = false;
        if(taskInfoOld == null){
            taskInfo.setCreateUser(UserUtil.getCurrentUserId());
            isSuccess = createScheduleJob(taskInfo);
        }
        else{
            taskInfo.setId(taskInfoOld.getId());
            taskInfo.setCreateUser(taskInfoOld.getCreateUser());
            taskInfo.setUpdateUser(UserUtil.getCurrentUserId());
            isSuccess = updateScheduleJob(taskInfo,taskInfoOld);
        }
        ResultObject result = isSuccess ?
                new ResultObject() :
                new ResultObject(ErrorCodeEnum.FAIL);
        return result;
    }
    public ResultObject update(TaskInfoReq taskInfoReq) {
        TaskInfo taskInfoOld = taskInfoDao.selectByPrimaryKey(taskInfoReq.getId());
        if(taskInfoOld == null){
            return new ResultObject(ErrorCodeEnum.UPDATE_RECORD_NOT_EXIST_ERROR);
        }
        TaskInfo taskInfo = ScheduleConvert.convert(taskInfoReq);
        boolean isSuccess = updateScheduleJob(taskInfo,taskInfoOld);
        ResultObject result = isSuccess ?
                new ResultObject() :
                new ResultObject(ErrorCodeEnum.FAIL);
        return result;
    }
    public ResultObject update(TaskInfo taskInfo) {
        TaskInfo taskInfoOld = taskInfoDao.selectUniqueness(taskInfo.getTaskName());
        if(taskInfoOld == null){
            return new ResultObject(ErrorCodeEnum.UPDATE_RECORD_NOT_EXIST_ERROR);
        }
        if(taskInfoOld.getId().intValue() == taskInfo.getId().intValue()){
            boolean isSuccess = taskInfoDao.updateByPrimaryKeySelective(taskInfo);
            ResultObject result = isSuccess ?
                    new ResultObject() :
                    new ResultObject(ErrorCodeEnum.FAIL);
            return result;
        }else{
            return new ResultObject(ErrorCodeEnum.UPDATE_RECORD_NOT_EXIST_ERROR);
        }
    }
    public ResultObject delete(int id) {
        TaskInfo taskInfo_old = taskInfoDao.selectByPrimaryKey(id);
        if (taskInfo_old == null) {
            return new ResultObject(ErrorCodeEnum.PARAM_EXCEPTION.format("数据不匹配"));
        }else{
            Boolean isSuccess = deleteScheduleJob(taskInfo_old);
            ResultObject result = isSuccess ?
                    new ResultObject() :
                    new ResultObject(ErrorCodeEnum.FAIL);

            return result;
        }
    }
    public ResultObject updatePreExecuteTime(int id) {
        Boolean isSuccess = taskInfoDao.updatePreExecuteTime(id);
        ResultObject result = isSuccess ?
                new ResultObject() :
                new ResultObject(ErrorCodeEnum.FAIL);
        return result;
    }
    public ResultObject selectAll(){
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setIsDelete((byte)0);
        List<TaskInfo> taskInfoList = taskInfoDao.selectManySelective(taskInfo);
        ResultObject result = new ResultObject() ;
        result.setData(taskInfoList);
        return result;
    }

    /**
     * @desc 获取分页列表
     */
    public ResultObject page(TaskInfoQueryReq taskInfoQueryReq, PageBounds pageBounds) {
        ResultObject result = new ResultObject();
        List<TaskInfo> taskList = taskInfoDao.selectWithPage(taskInfoQueryReq, pageBounds);
        List<TaskInfoResp> taskRespList= new ArrayList<TaskInfoResp>();
        for (TaskInfo task :
                taskList) {
            taskRespList.add(ScheduleConvert.convert(task));
        }
        result.setData(taskRespList);
        return result;
    }

    private TaskInfo selectUniqueness(String taskName) {
        return taskInfoDao.selectUniqueness(taskName);
    }

    /**
     * 任务注册
     */
    private boolean createScheduleJob(TaskInfo taskInfo) {
        boolean result = taskInfoDao.insertSelective(taskInfo);
        if(result){
            TaskInfo taskInfoNew = taskInfoDao.selectUniqueness(taskInfo.getTaskName());
            ScheduleManager.createScheduleJob(scheduler, taskInfoNew);
        }
        return result;
    }

    /**
     * 删除任务，同时要删除任务调度中的任务
     */
    private boolean deleteScheduleJob(TaskInfo taskInfo) {
        //任务是启用状态才需要在删除任务时同时取消调度
        if (taskInfo.getTaskStatus().intValue() == TaskStatus.ENABLED.getCode()) {
            ScheduleManager.deleteScheduleJob(scheduler, taskInfo.getTaskName(), taskInfo.getTaskGroup());
        }
        return taskInfoDao.deleteByPrimaryKey(taskInfo.getId());
    }

    /**
     * 更新任务调度中的任务，并更新表中数据
     */
    private boolean updateScheduleJob(TaskInfo taskInfo,TaskInfo taskInfoOld) {
        //代码注册进来，防止更改任务状态
        taskInfo.setTaskStatus(taskInfoOld.getTaskStatus());
        //任务是启用状态才需要在编辑任务时同时更新调度
        if (taskInfoOld.getTaskStatus().intValue() == TaskStatus.ENABLED.getCode()) {
            //先从旧的任务从调度器删除
            ScheduleManager.deleteScheduleJob(scheduler, taskInfoOld.getTaskName(), taskInfoOld.getTaskGroup());
            //再次添加到调度器
            ScheduleManager.createScheduleJob(scheduler, taskInfo);
        }
        return taskInfoDao.updateByPrimaryKeySelective(taskInfo);
    }

    /**
     * 启用任务：添加任务到调度器，并更改任务状态
     *
     * @param taskInfo
     * @return
     */
    public boolean enableTask(TaskInfo taskInfo) {
        TaskInfo dbTaskInfo = taskInfoDao.selectByPrimaryKey(taskInfo.getId());
        ScheduleManager.createScheduleJob(scheduler, dbTaskInfo);
        taskInfo.setTaskStatus((byte)TaskStatus.ENABLED.getCode());
        return taskInfoDao.updateByPrimaryKeySelective(taskInfo);
    }

    /**
     * 禁用任务：删除调度器中任务，并更改任务状态
     *
     * @param taskInfo
     * @return
     */
    public boolean disableTask(TaskInfo taskInfo) {
        TaskInfo dbTaskInfo = taskInfoDao.selectByPrimaryKey(taskInfo.getId());
        ScheduleManager.deleteScheduleJob(scheduler, dbTaskInfo.getTaskName(), dbTaskInfo.getTaskGroup());
        taskInfo.setTaskStatus((byte)TaskStatus.DISABLED.getCode());
        return taskInfoDao.updateByPrimaryKeySelective(taskInfo);
    }

    /**
     * 结束任务：只更改任务状态为结束
     * @param taskInfo
     * @return
     */
    public boolean endTask(TaskInfo taskInfo) {
        taskInfo.setTaskStatus((byte)TaskStatus.END.getCode());
        return taskInfoDao.updateByPrimaryKeySelective(taskInfo);
    }
}