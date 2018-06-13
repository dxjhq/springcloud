package com.hhly.api.constant;

import org.springframework.http.HttpMethod;

/** 项目中会用到的常量  */
public final class ConfigConst {
    /** 用户可修改预约时间次数 */
    public static final int USER_UPDATE_TOTAL = 1;

    public static final String LAWYER_OS = "律师平台";

    ///public static final String ADVICE_FEE="ADVICE_FEE";
    public static final String AM="AM";
    public static final String PM="PM";
    public static final String ALL="ALL";

    /** cors 支持的所有方法. 原则上来说, 国内复杂的浏览器环境, 开发时只支持 get 和 post 就好了, 其他忽略 */
    public static final String[] SUPPORT_METHODS = new String[] {
            HttpMethod.HEAD.name(),
            HttpMethod.GET.name(),
            HttpMethod.POST.name(),
            HttpMethod.PUT.name(),
            HttpMethod.DELETE.name(),
            HttpMethod.OPTIONS.name(),
            HttpMethod.PATCH.name(),
            HttpMethod.TRACE.name()
    };

    /**
     * 订单是否生成过提现记录 0 未生成 1 已生成 2 提现成功
     */
    public static  final int IS_DRAW_STATUS0 = 0;
    public static  final int IS_DRAW_STATUS1 = 1;
    public static  final int IS_DRAW_STATUS2 = 2;

    /***
     * 短信模板KEY
     */
    public static  final String USER_SMS_KEY_01 = "USER_SMS_KEY_01";
    public static  final String USER_SMS_KEY_02 = "USER_SMS_KEY_02";
    public static  final String LAWYER_SMS_KEY_03 = "LAWYER_SMS_KEY_03";
    public static  final String LAWYER_SMS_KEY_04 = "LAWYER_SMS_KEY_04";
    public static  final String LAWYER_SMS_KEY_05 = "LAWYER_SMS_KEY_05";

    public static  final String LAWYER_OS_TYPE5 = "邀请入驻奖励";
    public static  final String LAWYER_OS_TYPE6 = "业绩分成";

    /***
     * 默认系统用户ID
     */
    public  static  final long LAWYER_OS_USER = 1000l;
    public  static  final String LAWYER_OS_USER_NAME = "律正平台";
    public  static  final String LAWYER_OS_USER_PHONE = "0755-86338109";

    /**
     * 用户是否生成奖励过红包 0 否 1 有
     */
    public static  final int IS_STATE_STATUS0 = 0;
    public static  final int IS_STATE_STATUS1 = 1;
    /***
     * 订单暂订3单奖励20红包
     */
    public static  final String LAWYER_ORDER_KEY_COUNT = "LAWYER_ORDER_KEY_COUNT";
    public static  final String LAWYER_ORDER_MONEY_RMB = "LAWYER_ORDER_MONEY_RMB";
    /** 奖励红包费用的默认值, 单位: 元 */
    public static final String LAWYER_ORDER_MONEY_RMB_DEFAULT = "20";
    /** 奖励红包订单提现费用百分比 */
    public static final String LAWYER_RED_MONEY = "LAWYER_RED_MONEY";
    public static final String LAWYER_RED_MONEY_DEFAULT = "1";
    /** 平台邀请分成订单费用百分比 1-2000 百分之2 2001-10000 百分之5 10001以上 百分之10*/
    public static final String LAWYER_ORDER_MONEY = "LAWYER_ORDER_MONEY";
    public static final String LAWYER_ORDER_MONEY_DEFAULT = "2";
    public static final String LAWYER_ORDER_MONEY_05 = "LAWYER_ORDER_MONEY_05";
    public static final String LAWYER_ORDER_MONEY_DEFAULT_05= "5";
    public static final String LAWYER_ORDER_MONEY_10 = "LAWYER_ORDER_MONEY_10";
    public static final String LAWYER_ORDER_MONEY_DEFAULT_10 = "10";

    /***
     * 默认分成订单额度
     */
    public  static  final long LAWYER_01 = 100;
    public static final String LAWYER_MONEY_TOTAL = "LAWYER_MONEY_TOTAL";
    public static final String LAWYER_MONEY_TOTAL_DEFAULT = "2000";
    public static final String LAWYER_TOTAL_MONEY = "LAWYER_TOTAL_MONEY";
    public static final String LAWYER_TOTAL_MONEY_DEFAULT = "10000";

    /** 问卷调查抽奖活动开始时间 */
    public static final String QUESTION_ACTIVITY_START_TIME = "QUESTION_ACTIVITY_START_TIME";
    /** 问卷调查抽奖活动结束时间 */
    public static final String QUESTION_ACTIVITY_END_TIME = "QUESTION_ACTIVITY_END_TIME";
    /** 问卷调查活动用户中奖上限 */
    public static final String QUESTION_ACTIVITY_MAX_LIMIT_WINN = "QUESTION_ACTIVITY_MAX_LIMIT_WINN";

    /** POLYV直播系统的userId */
    public static final String LIVE_USER_ID = "LIVE_USER_ID";
    /** POLYV直播系统的appId */
    public static final String LIVE_APP_ID = "LIVE_APP_ID";
    /** POLYV直播系统的密钥 */
    public static final String LIVE_APP_SECRET = "LIVE_APP_SECRET";
}