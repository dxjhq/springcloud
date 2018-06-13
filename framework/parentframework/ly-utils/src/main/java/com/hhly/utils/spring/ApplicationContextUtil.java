package com.hhly.utils.spring;

import com.hhly.utils.LogUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 注入上下文对象
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        T result = null;
        if(context != null) {
            try {
                result = context.getBean(clazz);
            } catch (Exception var4) {
                LogUtil.ROOT_LOG.error("获取Bean失败！", var4);
            }
        }
        return result;
    }
    
    public static Object getBean(String beanId) {
        Object result = null;
        if(context != null) {
            try {
                result = context.getBean(beanId);
            } catch (Exception var4) {
                LogUtil.ROOT_LOG.error("获取Bean失败！", var4);
            }
        }
        return result;
    }

    public static <T> T getBean(String beanName, Class<T> requiredType) {
        T result = null;
        if(context != null) {
            try {
                result = context.getBean(beanName, requiredType);
            } catch (Exception var4) {
                LogUtil.ROOT_LOG.error("获取Bean失败！", var4);
            }
        }
        return result;
    }

    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationTag) throws BeansException{
        Map<String, Object> result = new HashMap<String, Object>();
        if(context != null) {
            try {
                result = context.getBeansWithAnnotation(annotationTag);
            } catch (Exception var4) {
                LogUtil.ROOT_LOG.error("获取Bean失败！", var4);
            }
        }
        return result;
    }
}
