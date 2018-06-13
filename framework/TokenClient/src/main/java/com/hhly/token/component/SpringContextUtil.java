package com.hhly.token.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class SpringContextUtil implements ApplicationContextAware {
    public static final Logger logger = LoggerFactory.getLogger("root");
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
                logger.error("获取Bean失败！", var4);
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
                logger.error("获取Bean失败！", var4);
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
                logger.error("获取Bean失败！", var4);
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
                logger.error("获取Bean失败！", var4);
            }
        }
        return result;
    }
}
