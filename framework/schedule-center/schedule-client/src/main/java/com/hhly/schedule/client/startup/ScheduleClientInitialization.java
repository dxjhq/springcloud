package com.hhly.schedule.client.startup;

import com.hhly.common.exception.ValidationException;
import com.hhly.common.dto.ErrorCodeEnum;
import com.hhly.common.dto.ResultObject;
import com.hhly.common.util.LogUtil;
import com.hhly.schedule.api.client.ScheduleClient;
import com.hhly.schedule.api.dto.request.TaskInfoReq;

import com.hhly.schedule.client.handler.ScheduleHandlerContainer;
import com.hhly.schedule.client.handler.ScheduleRegisterHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * client启动服务时向调度中心注册带@JobHandlerTag注解的任务
 * @author pengchao
 * @create 2017-09-19
 */
@Component
public class ScheduleClientInitialization implements ApplicationRunner {
    @Autowired
    private ScheduleRegisterHandler scheduleRegisterHandler;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        scheduleRegisterHandler.register();
    }
}