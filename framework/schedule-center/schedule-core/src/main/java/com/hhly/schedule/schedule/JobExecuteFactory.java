package com.hhly.schedule.schedule;

import com.hhly.common.util.ApplicationContextUtil;
import com.hhly.common.util.LogUtil;
import com.hhly.schedule.config.QuartzConfig;
import com.hhly.schedule.entity.TaskInfo;

import com.hhly.schedule.service.ScheduleExecute;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>任务执行器 </p>
 * 暂时未用到
 * @author pengchao
 * @create 2017-09-19
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class JobExecuteFactory implements Job{
    private static final Logger logger = LoggerFactory.getLogger(JobExecuteFactory.class);
    private final static ScheduleExecute scheduleExecute = ApplicationContextUtil.getBean("scheduleExecute",ScheduleExecute.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try{
            JobDataMap mergedJobDataMap = context.getMergedJobDataMap();
            TaskInfo taskInfo = (TaskInfo)mergedJobDataMap.get(QuartzConfig.TASK_INFO_KEY);
            logger.info(taskInfo.getTaskName());
            scheduleExecute.execute(taskInfo);
        }catch (Exception e){
            LogUtil.ROOT_LOG.error("执行失败",e);
        }
    }
}