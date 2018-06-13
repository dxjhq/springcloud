package com.hhly.utils.spring;

import com.hhly.utils.ValueUtil;
import com.hhly.utils.LogUtil;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanToMapUtil {

    /**
     * 将 Map 转化为 JavaBean
     *
     * @param map   包含属性值的 map
     * @param clazz 要转化的类型
     * @return 转化出来的 JavaBean 对象
     */
    public static <T> T map2Obj(Map map, Class<T> clazz) {
        try {
            // 调用无参构造
            T obj = clazz.newInstance();
            // 获取类属性
            for (PropertyDescriptor descriptor : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
                String propertyName = descriptor.getName();
                if (map.containsKey(propertyName)) {
                    // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值
                    descriptor.getWriteMethod().invoke(obj, map.get(propertyName));
                }
            }
            return obj;
        } catch (IntrospectionException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            if (LogUtil.ROOT_LOG.isWarnEnabled())
                LogUtil.ROOT_LOG.warn("map({}) --> bean({}) exception", map, clazz.getName(), e);
        }
        return null;
    }

    /**
     * 将 JavaBean 转化为 Map
     *
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     */
    public static <T> Map<String, Object> obj2Map(T bean) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        try {
            for (PropertyDescriptor descriptor : Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors()) {
                String propertyName = descriptor.getName();
                if (!"class".equals(propertyName)) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean);
                    if (ValueUtil.isNotBlank(result)) {
                        returnMap.put(propertyName, result);
                    } /*else {
                        returnMap.put(propertyName, "");
                    }*/
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            if (LogUtil.ROOT_LOG.isWarnEnabled())
                LogUtil.ROOT_LOG.warn("bean({}) --> map exception", bean, e);
        }
        return returnMap;
    }
}
