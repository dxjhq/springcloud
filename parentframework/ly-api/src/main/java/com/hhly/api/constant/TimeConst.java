package com.hhly.api.constant;

/**
 * @author pengchao
 * @create 2017-12-07
 * @desc 时间相关公共常量池
 */
public final class TimeConst {
    /**
     * 过期时间（秒）
     */
    public static long TIME_OUT_HALF_YEAR = 15552000L;
    public static long TIME_OUT_THREE_MONTH = 7776000L;//3600 * 24 * 90
    public static long TIME_OUT_MONTH = 2592000L;
    public static long TIME_OUT_HALF_MONTH = 1296000;
    public static long TIME_OUT_WEEK = 604800L;
    public static long TIME_OUT_THREE_DAY = 259200L;
    public static long TIME_OUT_DAY = 86400L;
    public static long TIME_OUT_TWO_HOUR = 7200L;
    public static long TIME_OUT_HOUR = 3600L;
    public static long TIME_OUT_HALF_HOUR = 1800L;

    /**
     * 订单自动确认时间间隔(天)
     */
    public static final int ORDER_AUTO_SURE = -7;
}