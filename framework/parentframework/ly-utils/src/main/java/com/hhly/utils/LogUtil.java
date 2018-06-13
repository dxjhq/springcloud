package com.hhly.utils;

import com.hhly.utils.date.DateFormatType;
import com.hhly.utils.web.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/** 日志管理, 使用此 utils 获取 log, 不要在类中使用 LoggerFactory.getLogger 的方式! */
public final class LogUtil {

    /** 根日志: 在类里面使用 LoggerFactory.getLogger(XXX.class) 跟这种方式一样! */
    public static final Logger ROOT_LOG = LoggerFactory.getLogger("root");

    /** SQL 相关的日志 */
    public static final Logger SQL_LOG = LoggerFactory.getLogger("sqlLog");

    public static final class Mdc {
        /** 接收到请求的时间. 在 log.xml 中使用 %X{receiveTime} 获取  */
        private static final String RECEIVE_TIME = "receiveTime";
        /** 请求信息, 包括有 ip、url, param 等  */
        private static final String REQUEST_INFO = "requestInfo";
        /** 请求里包含的头信息  */
        private static final String HEAD_INFO = "headInfo";

        /** 输出当前请求信息, 在日志中显示. 非线上时打印出头部参数 */
        public static void bind(Long id, String name) {
            String ip = RequestUtil.getRealIp();
            String method = RequestUtil.getRequest().getMethod();
            String url = RequestUtil.getRequestUrl();
            String param = RequestUtil.formatParam();

            MDC.put(RECEIVE_TIME, com.hhly.utils.date.DateUtil.formatDateToString(new java.util.Date(), DateFormatType.YYYY_MM_DD_HH_MM_SS_SSS.toString()) + " -> ");
            MDC.put(REQUEST_INFO, String.format("%s (%s,%s) (%s %s) param(%s)", ip, id, name, method, url, param));
            MDC.put(HEAD_INFO, String.format("header(%s)", RequestUtil.formatHeadParam()));
        }
        public static void unbind() {
            MDC.clear();
        }
    }
}
