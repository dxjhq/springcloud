package com.hhly.schedule.startup;

import com.hhly.common.dto.ResultObject;
import com.hhly.schedule.schedule.ScheduleManager;
import com.hhly.schedule.service.TaskInfoService;
import com.hhly.schedule.entity.TaskInfo;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.util.List;
/**
 * @author pengchao
 * @create 2017-09-19
 * @desc
 */
@Component
public class ScheduleCenterInitialization implements ApplicationRunner {

//    @Autowired
//    TaskInfoService taskInfoService;

    @Autowired
    Scheduler scheduler;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
//        ResultObject result= taskInfoService.selectAll();
//        if(result == null) return;
//        if(result.isSuccess()){
//            List<TaskInfo> list = (List<TaskInfo>)result.getData();
//            for (TaskInfo taskInfo : list) {
//
//            }
//        }
        scheduler.start();
        ScheduleManager.getAllJobs(scheduler);
    }
}
