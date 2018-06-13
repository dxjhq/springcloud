package com.hhly.api.enums;

import org.apache.commons.lang3.StringUtils;

/** app 访问的用户类型 */
public enum AppUserTypeEnum {
    User("用户端"), Lawyer("律师端");

    String value;
    AppUserTypeEnum(String value) {this.value = value;}
    public String getValue() {return value;}

    /**
     * <pre>
     * iOS:
     *   user:   LYLawyerPlatform/1.0.1 (iPhone; iOS 9.1; Scale/2.00)
     *   lawyer: LYLawyerPlatform_Lawyer/1.0.2 (iPhone; iOS 10.2; Scale/3.00)
     *
     * Android:
     *   user:   LYLawyerPlatform/1.0.2 Mozilla/5.0 (Linux; Android 5.1.1; NX511J Build/LMY47V; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/46.0.2490.76 Mobile Safari/537.36
     *   lawyer: LYLawyerPlatform_Lawyer/1.0.1 Mozilla/5.0 (Linux; Android 5.1.1; SM-G5500 Build/LMY47X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/49.0.2623.105 Mobile Safari/537.36
     * </pre>
     */
    public static AppUserTypeEnum get(String userAgent) {
        return StringUtils.isEmpty(userAgent) && userAgent.startsWith("LYLawyerPlatform_Lawyer") ? Lawyer : User;
    }
}
