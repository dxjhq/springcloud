package com.hhly.api.constant;

/**
 * @author pengchao
 * @create 2017-12-20
 * @desc 正则常量池
 */
public class RegexConst {
    /** 是否是移动端: https://gist.github.com/dalethedeveloper/1503252. Android 端使用的是 okHttp 这个组件 */
    public static final String MOBILE = "(?i)Mobile|iP(hone|od|ad)|okhttp|Android|BlackBerry|Blazer|PSP|UCWEB|IEMobile|Kindle|NetFront|Silk-Accelerated|(hpw|web)OS|Fennec|Minimo|Opera M(obi|ini)|Dol(f|ph)in|Skyfire|Zune";
    /** 是否是 iOS 端 */
    public static final String IOS = "(?i)iP(hone|od|ad)";
    /** 是否是 android 端. Android 端使用的是 okHttp 这个组件 */
    public static final String ANDROID = "(?i)Mobile|okhttp|Android";
    /** 是否是 pc 端 */
    public static final String PC = "(?i)AppleWebKit|Mozilla|Chrome|Safari|MSIE|Windows NT";
}
