package com.hhly.schedule.client.handler;

import com.hhly.common.date.DateUtil;
import com.hhly.common.dto.ErrorCodeEnum;
import com.hhly.common.dto.ResultObject;
import com.hhly.common.exception.ValidationException;
import com.hhly.common.util.LogUtil;
import com.hhly.schedule.api.client.ScheduleClient;
import com.hhly.schedule.api.dto.request.JobExecuteReq;
import com.hhly.schedule.api.dto.request.TaskExecuteLogReq;
import com.hhly.schedule.api.enums.TaskExecuteResult;
import com.hhly.schedule.api.enums.TriggerWay;
import com.hhly.schedule.client.annotation.ScheduleHandlerTag;
import org.aspectj.lang.JoinPoint;

import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author pengchao
 * @create 2017-09-30
 * @desc
 */
@Aspect // FOR AOP
@Component
public class ScheduleLogHandler {
    @Autowired
    private ScheduleClient scheduleClient;

    //任务开始时间
    private Timestamp startTime;
    //任务编号
    private int taskId;
    //任务名称
    private String taskName;


    @Pointcut("@annotation(com.hhly.schedule.client.annotation.ScheduleHandlerTag)")
    public void controllerAspect() {
    }

    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        //System.out.println("=====SysLogAspect前置通知开始=====");
        //System.out.println("joinPoint:" + joinPoint);
        //adminOptionContent(joinPoint);

        startTime = new Timestamp(System.currentTimeMillis());
        JobExecuteReq jobExecuteReq = getExecuteParam(joinPoint);
        if(jobExecuteReq != null){
            taskId = jobExecuteReq.getTaskId();
            taskName = jobExecuteReq.getTaskName();
        }
    }

    @After("controllerAspect()")
    public void doAfter(JoinPoint joinPoint) {
        //System.out.println("=====SysLogAspect后置通知开始=====");
        //System.out.println("joinPoint:" + joinPoint);
    }

    @AfterReturning(pointcut="controllerAspect()")
    public void doAfterReturning(JoinPoint joinPoint) {
        //System.out.println("=====SysLogAspect返回通知开始=====");
        //System.out.println("joinPoint:" + joinPoint);
        insertLog(createLog(TaskExecuteResult.SUCCESS.getCode(),TaskExecuteResult.SUCCESS.getResult(),""));
    }

    @AfterThrowing(value="controllerAspect() && @annotation(scheduleHandlerTag) ",throwing="e")
    public void doAfterThrowing(JoinPoint joinPoint,ScheduleHandlerTag scheduleHandlerTag,Exception e) {
        //System.out.println("=====SysLogAspect异常通知开始=====");
        //System.out.println("joinPoint:" + joinPoint);
        insertLog(createLog(TaskExecuteResult.FAIL.getCode(),TaskExecuteResult.FAIL.getResult(),e.getMessage()));
    }

    private void insertLog(TaskExecuteLogReq taskExecuteLogReq){
        try{
            ResultObject resultObject = scheduleClient.insertLog(taskExecuteLogReq);
            if (resultObject.getCode() == ErrorCodeEnum.SUCCESS.code()) {
                LogUtil.ROOT_LOG.info("Schedule execute log insert success, taskName: " + taskName);
            }else {
                LogUtil.ROOT_LOG.error("Schedule execute log insert failed, taskName: " + taskName);
            }
        }catch (Exception ex){
            LogUtil.ROOT_LOG.error("Schedule execute log insert failed, taskName: " + taskName);
        }
    }

    private TaskExecuteLogReq createLog(int scheduleExecuteResult, String executeLog, String scheduleExceptionMsg){
        TaskExecuteLogReq taskExecuteLogReq = new TaskExecuteLogReq();
        taskExecuteLogReq.setTaskId(taskId);
        taskExecuteLogReq.setScheduleStartTime(startTime);
        taskExecuteLogReq.setScheduleEndTime(new Timestamp(System.currentTimeMillis()));
        taskExecuteLogReq.setScheduleExecuteResult(scheduleExecuteResult);
        taskExecuteLogReq.setExecuteLog(executeLog);
        taskExecuteLogReq.setScheduleExceptionMsg(scheduleExceptionMsg);
        taskExecuteLogReq.setTriggerWay(TriggerWay.AUTO.getCode());
        return taskExecuteLogReq;
    }

    private JobExecuteReq getExecuteParam(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        for (Object info : args) {
            if (info instanceof JobExecuteReq){
                return ((JobExecuteReq) info);
            }
        }
        return null;
    }

    private void adminOptionContent(JoinPoint joinPoint){
        StringBuffer rs = new StringBuffer();
        String className = null;
        int index = 1;
        Object[] args = joinPoint.getArgs();

        for (Object info : args) {
            // 获取对象类型
            className = info.getClass().getName();
            className = className.substring(className.lastIndexOf(".") + 1);
            rs.append("[参数" + index + "，类型：" + className + "，值：");
            // 获取对象的所有方法
            Method[] methods = info.getClass().getDeclaredMethods();
            // 遍历方法，判断get方法
            for (Method method : methods) {
                String methodName = method.getName();
                System.out.println(methodName);
                // 判断是不是get方法
                if (methodName.indexOf("get") == -1) {// 不是get方法
                    continue;// 不处理
                }
                Object rsValue = null;
                try {
                    // 调用get方法，获取返回值
                    rsValue = method.invoke(info);
                    if (rsValue == null) {// 没有返回值
                        continue;
                    }
                } catch (Exception e) {
                    continue;
                }
                // 将值加入内容中
                rs.append("(" + methodName + " : " + rsValue + ")");
            }
            rs.append("]");
            index++;
        }
        System.out.println(rs.toString());
    }
}