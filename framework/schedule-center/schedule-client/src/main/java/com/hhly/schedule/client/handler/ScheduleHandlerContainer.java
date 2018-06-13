package com.hhly.schedule.client.handler;

import com.hhly.schedule.api.dto.request.TaskInfoReq;
import com.hhly.schedule.client.annotation.ScheduleHandlerTag;

import com.hhly.schedule.client.annotation.ScheduleTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>jobHandler容器，存储所有用@JobHandleTag注解了的handler</p>
 *
 * @author pengchao
 * @create 2017-09-19
 */
@Component
public class ScheduleHandlerContainer implements ApplicationContextAware {
    //private static final Logger logger = LoggerFactory.getLogger(ScheduleHandlerContainer.class);

    private static ConcurrentHashMap<String, Object> jobHandlerConcurrentHashMap = new ConcurrentHashMap<>();

    @Value("${spring.application.name}")
    private String serviceName;

    private static ConcurrentHashMap<String, TaskInfoReq> taskInfos = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        /*
        Map<String, Object> jobHandlerBeanMap = applicationContext.getBeansWithAnnotation(ScheduleTag.class);
        for (Object jobHandlerBean : jobHandlerBeanMap.values()) {
            Class clazz = jobHandlerBean.getClass();
            Method[] methods = clazz.getMethods();

            for(Method method : methods) {
                System.out.println(method.getName());

                if(!method.getName().contains("execute")) continue;

                TaskInfoReq taskInfo = new TaskInfoReq();
                taskInfo.setTaskGroup(serviceName);
                taskInfo.setRemoteServiceName(serviceName);

                if(method.isAnnotationPresent(ScheduleHandlerTag.class)){
                    ScheduleHandlerTag scheduleHandlerTag = method.getAnnotation(ScheduleHandlerTag.class);
                    taskInfo.setRemoteMethod(scheduleHandlerTag.scheduleMethod());
                    taskInfo.setRemotePath(scheduleHandlerTag.scheduleApi());
                    taskInfo.setCronExpression(scheduleHandlerTag.cronExpression());
                    taskInfo.setDescription(scheduleHandlerTag.description());
                    taskInfo.setExecuteParam(scheduleHandlerTag.paramsJson());
                    taskInfo.setExecuteParamApi(scheduleHandlerTag.paramsApi());
                }

                if(method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping mapping = method.getAnnotation(RequestMapping.class);

                    for(String url : mapping.value()) {
                        System.out.println(url);
                    }

                    for(RequestMethod item : mapping.method()) {
                        System.out.println(item);
                    }
                }

                if(StringUtils.isEmpty(taskInfo.getCronExpression())) continue;
                //Assert.hasText(taskInfo.getCronExpression(), "cronExpression required");
                //Assert.hasText(taskInfo.getRemotePath(), "remotePath required");

                String taskName = String.format("%s_%s_%s_%s", serviceName, clazz.getPackage().getName(), clazz.getSimpleName(),method.getName());

                jobHandlerConcurrentHashMap.put(taskName, jobHandlerBean);
                logger.info("put on jobHandler success , name:{}, jobHandler:{}", taskName, jobHandlerBean);

                taskInfo.setTaskName(taskName);
                taskInfos.put(taskName, taskInfo);
            }
        }
        */
    }

    public static Object getJobHandlerByName(String name) {
        return jobHandlerConcurrentHashMap.get(name);
    }

    public static ConcurrentHashMap<String, TaskInfoReq> getTaskInfos() {
        return taskInfos;
    }
    public static void setTaskInfos(TaskInfoReq taskInfoReq) {
        taskInfos.put(taskInfoReq.getTaskName(), taskInfoReq);
    }
}
