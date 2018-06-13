package com.hhly.schedule.client.handler;

import com.hhly.common.dto.ErrorCodeEnum;
import com.hhly.common.dto.ResultObject;
import com.hhly.common.exception.ValidationException;
import com.hhly.common.service.RedisService;
import com.hhly.common.util.ApplicationContextUtil;
import com.hhly.common.util.LogUtil;
import com.hhly.schedule.api.client.ScheduleClient;
import com.hhly.schedule.api.dto.request.TaskInfoReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pengchao
 * @create 2017-10-10
 * @desc
 */
@Component
public class ScheduleRegisterHandler {
    @Autowired
    private ScheduleClient scheduleClient;

    public void register(){
        ConcurrentHashMap<String, TaskInfoReq> taskInfos = ScheduleHandlerContainer.getTaskInfos();
        for (TaskInfoReq taskInfoReq : taskInfos.values()){
            try{
                ResultObject resultObject = scheduleClient.register(taskInfoReq);
                if (resultObject.getCode() == ErrorCodeEnum.SUCCESS.code()) {
                    LogUtil.ROOT_LOG.info("Schedule task register success, taskName: " + taskInfoReq.getTaskName());
                }else {
                    LogUtil.ROOT_LOG.error("Schedule task register failed,please retry!, taskName: " + taskInfoReq.getTaskName());
                    throw new ValidationException(resultObject.getMsg());
                }
            }catch (Exception ex){
                LogUtil.ROOT_LOG.error("Schedule task register failed,please retry!, taskName: " + taskInfoReq.getTaskName());
            }
        }
    }

    public void retry(){
        if(heart()){
            register();
            ScheduleHandlerContainer.setHeart(true);
        }
    }

    public boolean heart(){
        return scheduleClient.heart();
    }
}
