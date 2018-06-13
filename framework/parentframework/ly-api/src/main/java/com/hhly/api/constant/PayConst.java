package com.hhly.api.constant;
/**
 * @author pengchao
 * @create 2017-12-07
 * @desc 支付公共常量池
 */
public final class PayConst {
    /** 会员中心支付方式 16支付宝APP支付 2微信APP支付 12 银联手机网站支付 30工行E支付 10 微信支付查询结果 20 支付流水结果查询 1支付宝网页支付 */
    public static  final int ALIPAY_APP_KEY = 16;
    public static  final int WXPAY_KEY = 2;
    public static  final int UNIONPAY_APP_KEY = 12;
    public static  final int ICBCPAY_KEY = 30;
    public static  final int WXPAY_QUERY = 10;
    public static  final int PAYORDER_QUERY = 20;
    public static  final int ALIPAY_TYPE_BUY = 1;
    public static  final int WXPAY_KEY_N = 3;

    /*** 支付宝网页支付服务接口 ***/
    public static  final String ALIPAY_SERVICE ="create_direct_pay_by_user";

    /*** 微信支付类型 JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付 ***/
    public static  final String WXPAY_APP = "APP";
    public static  final String WXPAY_NATIVE = "NATIVE";
    public static  final String WXPAY_JSAPI = "JSAPI";
    public static  final String WXPAY_APP_ID = "WXPAY_APP_ID";

    /**律师平台支付方式 1支付宝 APP支付 2 微信APP支付 3 支付宝 PC端支付 4微信扫码支付 5微信小程序支付 6威富通微信扫码支付 7 威富通支付宝扫码支付**/
    public static  final int BUY_ALIPAY= 1;
    public static  final int BUY_WXPAY = 2;
    public static  final int BUY_ALIPAY_PC= 3;
    public static  final int BUY_WXPAY_N = 4;
    public static  final int BUY_WXPAY_JSAPI = 5;
    public static  final int BUY_SWIFT_WXPAY = 6;
    public static  final int BUY_SWIFT_ALIPAY = 7;

    /***
     * 费用类型
     */
    public static  final String LAWYER_OS_TYPE1 = "华海律正支付费用";
    public static  final String LAWYER_OS_TYPE2 = "华海律正支付费用";
    public static  final String LAWYER_OS_TYPE3 = "华海律正支付费用";
    public static  final String LAWYER_OS_TYPE4 = "华海律正支付费用";

    /** 平台收取费用百分比 */
    public static final String LAWYER_MONEY = "LAWYER_MONEY";
    public static final String LAWYER_MONEY_DEFAULT = "10";

    /** 用户与律师建立连接的免费时长, 单位: 分钟 */
    public static final String FREE_MINUTES = "FREE_MINUTES";
    public static final String FREE_MINUTES_DEFAULT = "30";

    /**  律师设定文书价格最低值 ，单位： 元*/
    public static final String FILE_PRICE = "FILE_PRICE";
    public static final String FILE_PRICE_DEFAULT = "300";

    // ====================================================================================

    /**
     * 银行卡删除标识 是否删除 0是 1 否
     */
    public static  final long BANK_CARD_DEL_YES = 0;
    public static  final long BANK_CARD_DEL_NO = 1;
    /**
     * 银行卡状态  1创建审核中 2审核成功 3审核失败
     */
    public static  final int BANK_CARD_STATUS1 = 1;
    public static  final int BANK_CARD_STATUS2 = 2;
    public static  final int BANK_CARD_STATUS3 = 3;

    /**
     * 支付状态  0失败 1成功 2 未支付 3 交易关闭 -1 订单不存在
     */
    public static  final int PAY_STATUS0 = 0;
    public static  final int PAY_STATUS1 = 1;
    public static  final int PAY_STATUS2 = 2;
    public static  final int PAY_STATUS3 = 3;

    /**
     * 提现的状态 1 创建提现中 2提现成功 3提现失败
     */
    public static  final int DRAW_STATUS1 = 1;
    public static  final int DRAW_STATUS2 = 2;
    public static  final int DRAW_STATUS3 = 3;
    /***
     * 银联代付KEY
     */
    public static  final String UNPAY_MERCH_ID = "UNPAY_MERCH_ID";

    public static  final String LAWYER_OS_CASE= "华海律正平台转账";


    /**
     * 微信扫描支付状态
     * REFUND—转入退款
     * NOTPAY—未支付
     * CLOSED—已关闭
     * REVOKED—已撤销（刷卡支付）
     * USERPAYING--用户支付中
     * PAYERROR--支付失败(其他原因，如银行返回失败)
     * SUCCESS—支付成功
     */
    public static  final String WXPAY_SUCCESS = "SUCCESS";
    public static  final String WXPAY_REFUND = "REFUND";
    public static  final String WXPAY_NOTPAY = "NOTPAY";
    public static  final String WXPAY_CLOSED = "CLOSED";
    public static  final String WXPAY_USERPAYING = "USERPAYING";
    public static  final String WXPAY_PAYERROR = "PAYERROR";


    /***
     * 微信支付查询状态
        SUCCESS—支付成功
        REFUND—转入退款
        NOTPAY—未支付
        CLOSED—已关闭
        REVOKED—已撤销（刷卡支付）
        USERPAYING--用户支付中
        PAYERROR--支付失败(其他原因，如银行返回失败)
    */

    public static final String WX_PAY_SUCCESS= "SUCCESS";
    public static final String WX_PAY_REFUND = "REFUND";
    public static final String WX_PAY_NOTPAY = "NOTPAY";
    public static final String WX_PAY_CLOSED = "CLOSED";
    public static final String WX_PAY_REVOKED = "REVOKED";
    public static final String WX_PAY_USERPAYING = "USERPAYING";
    public static final String WX_PAY_PAYERROR = "PAYERROR";

    /**竞标默认流标时间**/
    public static final String USER_CHECK_QUOTE = "-7";
    public static final String USER_CHECK_QUOTE_NAME = "CHECK_QUOTE";

    /** 威富通商户号 */
    public static final String SWFIT_PAY_MECH_ID = "SWFIT_PAY_MECH_ID";
    public static final String SWFIT_PAY_MECH_ID_DEFAULT = "102510469384";

    /** 威富通PAY_URL */
    public static final String SWFIT_PAY_URL = "SWFIT_PAY_URL";
    public static final String SWFIT_PAY_URL_DEFAULT = "https://pay.swiftpass.cn/pay/gateway";

    /** 威富通KEY_API*/
    public static final String SWFIT_KEY_API = "SWFIT_KEY_API";
    public static final String SWFIT_KEY_API_DEFAULT = "10a6dae60694e915e5f185630838845c";

    /** 威富通支付完成后 微信支付回调地址*/
    public static final String WXPAY_NOTITY_URL = "WXPAY_NOTITY_URL";
    public static final String PAY_NOTITY_URL_DEFAULT = "http://183.61.172.81:7082/api/swiftWxPayNotify";

    /** 威富通支付完成后 支付宝回调地址*/
    public static final String ALI_NOTIFY_URL = "ALI_NOTIFY_URL";
    public static final String ALI_NOTIFY_URL_DEFAULT = "http://183.61.172.81:7082/api/swiftAliPayNotify";

}