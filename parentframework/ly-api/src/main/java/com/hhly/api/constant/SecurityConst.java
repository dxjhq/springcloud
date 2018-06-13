package com.hhly.api.constant;

/**
 * @author pengchao
 * @create 2017-12-07
 * @desc 安全，账户相关公共常量池
 */
public class SecurityConst {

    public final static String SECURITY_KEY = "security";
    public static final String SECURITY_TOKEN = "token";
    public final static String SECURITY_PROFILE = "profiles";
    public final static String SECURITY_PROFILE_INDEX = "profileIndex";

    /**
     * profile attribute
     */
    public final static String PROFILE_ATTRIBUTE_KEY_USERNAME = "username";
    public final static String PROFILE_ATTRIBUTE_KEY_USERID = "userid";
    public final static String PROFILE_ATTRIBUTE_KEY_ACCOUNT = "account";
    public final static String PROFILE_ATTRIBUTE_KEY_CELLPHONE = "cellphone";
    public final static String PROFILE_ATTRIBUTE_KEY_NANOTIME = "nanoTime";
    public final static String PROFILE_ATTRIBUTE_KEY_EXPIRESTIME = "expireTime";
    public final static String PROFILE_ATTRIBUTE_KEY_SESSION = "key";
    public final static String PROFILE_KEY_CHECK_SUM = "check_sum";

    //兼容旧系统
    public final static String PROFILE_KEY_PREFIX_TOKEN = "gateway:";
    public final static String PROFILE_KEY_PREFIX_PROFILE = "profile:";

    /**
     * 安全
     */
    public final static String SECURITY_KEY_PREFIX_SECURITY = "security:";
    public final static String SECURITY_KEY_PREFIX_BLACKLIST = SECURITY_KEY_PREFIX_SECURITY + "blcakList";
    public final static String SECURITY_KEY_PREFIX_WHITELIST = SECURITY_KEY_PREFIX_SECURITY + "whiteList";
}