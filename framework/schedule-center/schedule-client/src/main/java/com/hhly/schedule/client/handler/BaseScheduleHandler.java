package com.hhly.schedule.client.handler;

import com.hhly.schedule.api.dto.request.TaskInfoReq;
import com.hhly.schedule.client.annotation.ScheduleHandlerTag;
import com.hhly.schedule.client.annotation.ScheduleTag;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pengchao
 * @create 2017-09-30
 * @desc
 */
@RestController
public class BaseScheduleHandler implements BeanPostProcessor {
    @Value("${spring.application.name}")
    private String serviceName;


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        ScheduleTag scheduleTag = AnnotationUtils.findAnnotation(bean.getClass(), ScheduleTag.class);
        if(scheduleTag == null){
            return bean;
        }
        System.out.println("[" + scheduleTag + "]" + bean.toString());
        ConcurrentHashMap<String, TaskInfoReq> taskList = ScheduleHandlerContainer.getTaskInfos();

        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        if (methods != null) {
            //生成任务列表
            for (Method method : methods) {
                ScheduleHandlerTag scheduleHandlerTag = AnnotationUtils.findAnnotation(method, ScheduleHandlerTag.class);
                if (scheduleHandlerTag == null) continue;

                String remotePath = "";
                String remoteMethod = "";

                RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
                if (requestMapping != null) {
                    String[] urlList = requestMapping.value();
                    if (urlList.length > 0) {
                        remotePath = urlList[0];
                    }
                    RequestMethod[] methodList = requestMapping.method();
                    if (methodList.length > 0) {
                        remoteMethod = methodList[0].toString();
                    }
                }

                remotePath = StringUtils.isEmpty(remotePath)?scheduleHandlerTag.scheduleApi():remotePath;
                remoteMethod = StringUtils.isEmpty(remoteMethod)?scheduleHandlerTag.scheduleMethod():remoteMethod;

                if(StringUtils.isEmpty(remotePath)) continue;

                String taskName = ("/".equals(remotePath.substring(0,1)))?
                        String.format("%s_%s", serviceName, remotePath.substring(1).replace('/','-')):
                        String.format("%s_%s", serviceName, remotePath.replace('/','-'));

                TaskInfoReq taskInfoReq = taskList.get(taskName);
                if(taskInfoReq != null) continue;

                TaskInfoReq taskInfo = new TaskInfoReq();
                taskInfo.setTaskGroup(serviceName);
                taskInfo.setRemoteServiceName(serviceName);
                taskInfo.setTaskName(taskName);
                taskInfo.setRemoteMethod(remoteMethod);
                taskInfo.setRemotePath(remotePath);
                taskInfo.setCronExpression(scheduleHandlerTag.cronExpression());
                taskInfo.setDescription(scheduleHandlerTag.description());
                taskInfo.setExecuteParam(scheduleHandlerTag.paramsJson());
                taskInfo.setExecuteParamApi(scheduleHandlerTag.paramsApi());
                System.out.println(taskName);
                ScheduleHandlerContainer.setTaskInfos(taskInfo);
            }
        }
        return bean;
    }
}