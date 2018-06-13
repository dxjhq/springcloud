package com.hhly.token.service;

import com.hhly.token.model.Context;

/**
 * 上下文工具
 * @author pengchao
 * @create 2017-12-14
 * @desc
 */
public class ContextUtil {
    private static ThreadLocal<Context> currentLocalContext = new InheritableThreadLocal<Context>();

    public static Context get() {
        return currentLocalContext.get();
    }

    public static void set(Context context) {
        currentLocalContext.set(context);
    }

    public static void unset() {
        currentLocalContext.remove();
    }

    public static void addGlobalVariable(String key, Object value) {
        Context context = get();
        if (context == null) {
            set(new Context());
            context = get();
        }

        context.addGlobalVariable(key, value);
    }

    public Object getGlobalVariable(String key) {
        Object result = null;

        Context context = get();
        if (context != null) {
            result = context.getGlobalVariable(key);
        }

        return result;
    }
}