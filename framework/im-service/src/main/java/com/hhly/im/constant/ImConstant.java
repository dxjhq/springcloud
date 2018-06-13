package com.hhly.im.constant;

/**
 * @author wangxianchen
 * @create 2017-10-24
 * @desc 消息服务常量类
 */
public class ImConstant {

    /**
     * 短信在redis里存储的key的前缀
     */
    public static final String SMS_SEND_RECORD_PREFIX = "sms_record:";

    /**
     * 短信记录存储在redis里的超时时间,单位秒,默认为1天
     */
    public static final long SMS_SEND_RECORD_EXPIRE_TIME = 86400L;

    /**
     *短信推送到网关成功标识
     */
    public static final String SMS_PUSH_OUT_SUCCESS = "0";
}
