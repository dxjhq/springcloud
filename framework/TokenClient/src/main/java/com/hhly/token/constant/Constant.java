package com.hhly.token.constant;

/**
 * @author pengchao
 * @create 2017-12-06
 * @desc Token相关常量池
 */
public class Constant {
    /**
     * 过期时间（秒）
     */
    public static long TIME_OUT_HALF_YEAR = 15552000L;
    public static long TIME_OUT_THREE_MONTH = 7776000L;
    public static long TIME_OUT_MONTH = 2592000L;
    public static long TIME_OUT_HALF_MONTH = 1296000;
    public static long TIME_OUT_WEEK = 604800L;
    public static long TIME_OUT_THREE_DAY = 259200L;
    public static long TIME_OUT_DAY = 86400L;
    public static long TIME_OUT_TWO_HOUR = 7200L;
    public static long TIME_OUT_HOUR = 3600L;
    public static long TIME_OUT_HALF_HOUR = 1800L;

    /**
     * profiles
     */
    public final static String PROFILE_KEY ="profiles";
    public final static String PROFILE_INDEX ="protectedIndex";

    public final static String REQUEST_HEADER_KEY_TOKEN = "token";
    /**
     * profile attribute
     */
    public final static String PROFILE_ATTRIBUTE_KEY_USERNAME = "username";
    public final static String PROFILE_ATTRIBUTE_KEY_USERID = "userid";
    public final static String PROFILE_ATTRIBUTE_KEY_ACCOUNT = "account";
    public final static String PROFILE_ATTRIBUTE_KEY_CELLPHONE = "cellphone";
    public final static String PROFILE_ATTRIBUTE_KEY_NANOTIME="nanoTime";
    public final static String PROFILE_ATTRIBUTE_KEY_SESSION = "key";
    public final static String PROFILE_ATTRIBUTE_KEY_CHECKSUM = "checkSum";
    public final static String PROFILE_ATTRIBUTE_KEY_SID = "sid";
    /**
     * cache
     */
    public final static String CACHE_KEY_PREFIX_TOKEN = "gateway:";
    public final static String CACHE_KEY_PREFIX_PROFILE = "profile:";
    public final static String CACHE_KEY_EXPIRES_IN = "expire_time";


    public static String CACHE_KEY_REDIS_HOST = "";
    public static String CACHE_KEY_REDIS_USER = "";
}
