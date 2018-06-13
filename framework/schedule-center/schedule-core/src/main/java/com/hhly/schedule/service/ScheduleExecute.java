package com.hhly.schedule.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.hhly.common.dto.ResultObject;
import com.hhly.common.service.AbstractBaseRestService;
import com.hhly.common.util.LogUtil;
import com.hhly.common.util.RequestUtils;

import com.hhly.schedule.api.constant.ScheduleUrl;
import com.hhly.schedule.api.enums.TaskExecuteResult;
import com.hhly.schedule.api.enums.TriggerWay;
import com.hhly.schedule.dao.TaskExecuteLogDao;
import com.hhly.schedule.dao.TaskInfoDao;
import com.hhly.schedule.entity.TaskExecuteLog;
import com.hhly.schedule.entity.TaskInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pengchao
 * @create 2017-09-28
 * @desc
 */
@Service("scheduleExecute")
public class ScheduleExecute extends AbstractBaseRestService {
    final Gson gson = new Gson();
    @Autowired
    private TaskInfoDao taskInfoDao;

    @Autowired
    private TaskExecuteLogService taskExecuteLogService;

    @Override
    public String buildURL(String uri) {
        return uri;
    }

    public ResultObject execute(TaskInfo taskInfo) {
        HttpHeaders requestHeaders = RequestUtils.createHeader();

        //组装参数
        String jsonParam = taskInfo.getExecuteParam();
        if(StringUtils.isEmpty(jsonParam)){
            jsonParam = "{\"taskId\":\"" + taskInfo.getId() + "\",\"taskName\":\"" + taskInfo.getTaskName() + "\"}";
        }else{
            jsonParam = "{\"taskId\":\"" + taskInfo.getId() + "\",\"taskName\":\"" + taskInfo.getTaskName() + "\"," + jsonParam.substring(1);
        }
        //更新最近执行时间
        taskInfoDao.updatePreExecuteTime(taskInfo.getId());

        HttpEntity<String> httpEntity =
                new HttpEntity<String>(jsonParam, requestHeaders);

        String jsonResponse = "";
        String scheduleUri = "http://" + taskInfo.getRemoteServiceName().toUpperCase() + taskInfo.getRemotePath();
        try{
            switch (taskInfo.getRemoteMethod().toUpperCase()) {
                case "POST": {
                    jsonResponse = this.post(scheduleUri, httpEntity);
                    break;
                }
                case "GET": {
                    Map<String, String> parmas = JSONObject.parseObject(jsonParam, Map.class);
                    jsonResponse = this.get(scheduleUri, httpEntity, parmas);
                    break;
                }
            }
        }
        catch (Exception ex){
            //No instances available
            //ex.printStackTrace();
            String exceptionMsg = taskInfo.getTaskName() + "调度失败，失败原因:" + ex.getMessage();
            LogUtil.ROOT_LOG.info(exceptionMsg);
            TaskExecuteLog taskExecuteLog = taskExecuteLogService.buildEntity(taskInfo.getId(), TaskExecuteResult.FAIL.getCode(), TriggerWay.AUTO.getCode(),exceptionMsg, TaskExecuteResult.FAIL.getResult());
            taskExecuteLogService.add(taskExecuteLog);
        }

        return (ResultObject) this.gson.fromJson(jsonResponse, ResultObject.class);
    }
}