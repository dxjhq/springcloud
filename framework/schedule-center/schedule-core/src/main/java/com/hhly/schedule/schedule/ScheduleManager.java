package com.hhly.schedule.schedule;

import com.hhly.schedule.config.QuartzConfig;
import com.hhly.common.dto.ErrorCodeEnum;
import com.hhly.common.exception.ValidationException;
import com.hhly.schedule.api.enums.TriggerWay;
import com.hhly.schedule.entity.TaskInfo;

import org.quartz.*;

import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * <p>调度器管理工具类</p>
 *
 * @author pengchao
 * @createTime 2017-09-25
 */
public class ScheduleManager {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleManager.class);

    /**
     * 将任务加入调度器
     *
     * @param scheduler 调度器实例
     * @param taskInfo  任务详情
     */
    public static void createScheduleJob(Scheduler scheduler, TaskInfo taskInfo) {
        createScheduleJob(scheduler, taskInfo.getTaskName(), taskInfo.getTaskGroup(),
                taskInfo.getCronExpression(), taskInfo);
    }

    /**
     * 创建定时任务
     *
     * @param scheduler      调度器
     * @param jobName        任务名称
     * @param jobGroup       任务组名
     * @param cronExpression cron表达示
     * @param param          任务运行时参数
     */
    private static void createScheduleJob(
            Scheduler scheduler, String jobName, String jobGroup,
            String cronExpression, Object param) {
        //同步或异步
        //Class<? extends Job> jobClass = isSync ? JobSyncExecuteFactory.class : JobExecuteFactory.class;
        Class<? extends Job> jobClass = JobExecuteFactory.class;
        //构建job信息，每次jobDetail都不同
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();

        //将任务信息传入执行工厂，运行时的方法可以获取
        jobDetail.getJobDataMap().put(QuartzConfig.TASK_INFO_KEY, param);
        jobDetail.getJobDataMap().put(QuartzConfig.TRIGGER_WAY, TriggerWay.AUTO.getCode());
        //表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
                .withSchedule(scheduleBuilder).build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            logger.error("task add to scheduler failed", e);
            //throw new ApplicationException(ErrorCodeEnum.TASK_ADD_TO_SCHEDULE_ERROR);
        }
    }

    /**
     * 获取jobKey
     *
     * @param jobName  任务名称
     * @param jobGroup the job group
     * @return the job key
     */
    private static JobKey getJobKey(String jobName, String jobGroup) {
        return JobKey.jobKey(jobName, jobGroup);
    }

    /**
     * 获取triggerKey
     *
     * @param triggerName  触发器名称
     * @param triggerGroup 触发器组名
     * @return TriggerKey triggerKey
     */
    private static TriggerKey getTriggerKey(String triggerName, String triggerGroup) {
        return TriggerKey.triggerKey(triggerName, triggerGroup);
    }

    /**
     * 恢复任务
     *
     * @param scheduler 调度器实例
     * @param jobName   任务名称
     * @param jobGroup  任务组名
     */
    public static void resumeJob(Scheduler scheduler, String jobName, String jobGroup) {

        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            scheduler.resumeJob(jobKey);
        } catch (Exception e) {
            logger.error("resume job failed", e);
            //throw new ApplicationException(ErrorCodeEnum.TASK_RESUME_ERROR);
        }
    }

    /**
     * 暂停任务
     *
     * @param scheduler 调度器实例
     * @param jobName   任务名称
     * @param jobGroup  任务组名
     */
    public static void pauseJob(Scheduler scheduler, String jobName, String jobGroup) {

        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            scheduler.pauseJob(jobKey);
        } catch (Exception e) {
            logger.error("pause job failed", e);
            //throw new ApplicationException(ErrorCodeEnum.TASK_PAUSE_ERROR);
        }
    }

    /**
     * 立即运行一次任务
     *
     * @param scheduler 调度器实例
     * @param jobName   任务名称
     * @param jobGroup  任务组名
     */
    public static void runOnce(Scheduler scheduler, String jobName, String jobGroup) {
        JobKey jobKey = getJobKey(jobName, jobGroup);
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(QuartzConfig.TRIGGER_WAY, TriggerWay.MANUAL.getCode());
        try {
            scheduler.triggerJob(jobKey, jobDataMap);
        } catch (Exception e) {
            logger.error("job run once failed", e);
            //throw new ApplicationException(ErrorCodeEnum.TASK_RUN_ONCE);
        }
    }

    /**
     * 获取表达式触发器
     *
     * @param scheduler    the scheduler
     * @param triggerName  the trigger name
     * @param triggerGroup the trigger group
     * @return CronTrigger
     */
    private static CronTrigger getCronTrigger(Scheduler scheduler, String triggerName, String triggerGroup) {
        TriggerKey triggerKey = getTriggerKey(triggerName, triggerGroup);
        return getCronTrigger(scheduler, triggerKey);
    }

    /**
     * 获取表达式触发器
     *
     * @param scheduler  the scheduler
     * @param triggerKey the triggerKey
     * @return CronTrigger
     */
    private static CronTrigger getCronTrigger(Scheduler scheduler, TriggerKey triggerKey) {
        try {
            return (CronTrigger) scheduler.getTrigger(triggerKey);
        } catch (Exception e) {
            logger.error("get trigger by key error", e);
            throw new ValidationException(ErrorCodeEnum.FAIL);
            //throw new ApplicationException(ErrorCodeEnum.GET_TRIGGER_BY_KEY_ERROR);
        }
    }

    /**
     * 更新Trigger cron表达式
     *
     * @param scheduler         调度器实例
     * @param triggerName       trigger name
     * @param triggerGroup      trigger group
     * @param newCronExpression 新的cron表达式
     */
    public static void updateJobTrigger(Scheduler scheduler, String triggerName, String triggerGroup, String newCronExpression) {
        try {
            TriggerKey triggerKey = getTriggerKey(triggerName, triggerGroup);
            //表达式构建器,构建新表达式
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(newCronExpression);

            CronTrigger trigger = getCronTrigger(scheduler, triggerName, triggerGroup);

            //按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            //按新的trigger重设trigger
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("update job trigger failed", e);
            //throw new ApplicationException(ErrorCodeEnum.UPDATE_JOB_TRIGGER_ERROR);
        }
    }

    /**
     * 删除定时任务
     *
     * @param scheduler 调度器实例
     * @param jobName   任务名
     * @param jobGroup  任务组
     */
    public static void deleteScheduleJob(Scheduler scheduler, String jobName, String jobGroup) {
        try {
            scheduler.deleteJob(getJobKey(jobName, jobGroup));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除定时任务失败", e);
            //throw new ApplicationException(ErrorCodeEnum.DELETE_JOB_ERROR_FROM_SCHEDULE_ERROR);
        }
    }

    public static void getAllJobs(Scheduler scheduler){
        try{
            //Scheduler scheduler = schedulerFactory.getScheduler();
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    String jobName = jobKey.getName();
                    String jobGroup = jobKey.getGroup();

                    //get job's trigger
                    List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                    Date nextFireTime = triggers.get(0).getNextFireTime();

                    System.out.println("[jobName] : " + jobName + " [groupName] : "
                            + jobGroup + " - " + nextFireTime);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取所有计划中的任务列表
     * @return
     */
    public List<Map<String,Object>> queryAllJob(Scheduler scheduler){
        List<Map<String,Object>> jobList=null;
        try {
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            jobList = new ArrayList<Map<String,Object>>();
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put("jobName",jobKey.getName());
                    map.put("jobGroupName",jobKey.getGroup());
                    map.put("description","触发器:" + trigger.getKey());
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    map.put("jobStatus",triggerState.name());
                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        String cronExpression = cronTrigger.getCronExpression();
                        map.put("jobTime",cronExpression);
                    }
                    jobList.add(map);
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return jobList;
    }

    /**
     * 获取所有正在运行的job
     * @return
     */
    public List<Map<String,Object>> queryRunJon(Scheduler scheduler){
        List<Map<String,Object>> jobList=null;
        try {
            List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
            jobList = new ArrayList<Map<String,Object>>(executingJobs.size());
            for (JobExecutionContext executingJob : executingJobs) {
                Map<String,Object> map=new HashMap<String, Object>();
                JobDetail jobDetail = executingJob.getJobDetail();
                JobKey jobKey = jobDetail.getKey();
                Trigger trigger = executingJob.getTrigger();
                map.put("jobName",jobKey.getName());
                map.put("jobGroupName",jobKey.getGroup());
                map.put("description","触发器:" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                map.put("jobStatus",triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    map.put("jobTime",cronExpression);
                }
                jobList.add(map);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return jobList;
    }
}
