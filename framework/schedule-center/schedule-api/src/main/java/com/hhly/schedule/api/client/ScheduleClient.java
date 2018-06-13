package com.hhly.schedule.api.client;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.hhly.common.dto.ResultObject;
import com.hhly.common.service.AbstractBaseRestService;
import com.hhly.common.util.RequestUtils;
import com.hhly.schedule.api.constant.ScheduleUrl;
import com.hhly.schedule.api.dto.request.TaskExecuteLogReq;
import com.hhly.schedule.api.dto.request.TaskInfoReq;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengchao
 * @create 2017-09-13
 * @desc
 */
@Service
public class ScheduleClient extends AbstractBaseRestService {
    final Gson gson = new Gson();

    @Override
    public String buildURL(String s) {
        return ScheduleUrl.buildURL(s);
    }

    //任务注册
    public ResultObject register(TaskInfoReq taskInfoReq) {
        HttpHeaders requestHeaders = RequestUtils.createHeader();
        HttpEntity<String> httpEntity=
                new HttpEntity<String>(JSON.toJSONString(taskInfoReq), requestHeaders);
        String json = this.post(ScheduleUrl.SCHEDULE_TASK_REGISTER, httpEntity);
        return (ResultObject)this.gson.fromJson(json, ResultObject.class);
    }

    //插入任务执行日志
    public ResultObject insertLog(TaskExecuteLogReq taskExecuteLogReq) {
        HttpHeaders requestHeaders = RequestUtils.createHeader();
        HttpEntity<String> httpEntity=
                new HttpEntity<String>(JSON.toJSONString(taskExecuteLogReq), requestHeaders);
        String json = this.post(ScheduleUrl.EXECUTE_LOG_ADD, httpEntity);
        return (ResultObject)this.gson.fromJson(json, ResultObject.class);
    }

    //更新任务执行日志
    public ResultObject updateLog(TaskExecuteLogReq taskExecuteLogReq) {
        HttpHeaders requestHeaders = RequestUtils.createHeader();
        HttpEntity<String> httpEntity=
                new HttpEntity<String>(JSON.toJSONString(taskExecuteLogReq), requestHeaders);
        String json = this.post(ScheduleUrl.EXECUTE_LOG_UPDATE, httpEntity);
        return (ResultObject)this.gson.fromJson(json, ResultObject.class);
    }
}