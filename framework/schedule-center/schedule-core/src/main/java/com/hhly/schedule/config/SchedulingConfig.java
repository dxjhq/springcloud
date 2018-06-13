package com.hhly.schedule.config;

import com.hhly.schedule.schedule.ScheduleManager;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author pengchao
 * @create 2017-09-29
 * @desc 用于定时清理宕机的任务
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Scheduled(cron = "${scheduled.sync.session.cron}")
    public void scheduler() {

    }
}