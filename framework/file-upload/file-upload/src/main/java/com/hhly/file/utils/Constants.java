package com.hhly.file.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量表
 *
 * @author BSW
 * @create 2017-09-28
 *
 * version 1.0
 */
public interface Constants {
    /**
     * 异常信息统一头信息<br>
     * 非常遗憾的通知您,程序发生了异常
     */
    public static final String Exception_Head = "系统异常";
    /**
     * 缓存键值
     */
    public static final Map<Class<?>, String> cacheKeyMap = new HashMap<>();
    /**
     * 保存文件所在路径的key，FILE_MD5:fb6fa6cfeb9e1a35eb7a62ef540c2fac
     */
    public static final String FILE_MD5_KEY = "FILE_MD5:";

    /**
     * 保存上传文件的状态
     */
    public static final String FILE_UPLOAD_STATUS = "FILE_UPLOAD_STATUS";

}
