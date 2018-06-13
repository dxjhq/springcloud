package com.hhly.api.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * app版本
 */
public enum VersionEnum {
    // 1.0 ==> 100   1.0.1 ==> 101   1.0.2 ===> 102   1.0.3 ==> 103   2.0 ==> 200   依此类推
    // !!!保证大版本的 code 比小版本的 code 要大(比如 V3 的 code 是 300, 如果设定成 30 比 V2 的 200 小了, 将会出问题)!!!
    // !!!这一点很重要!!!
    V1(100, "1.0"),
    V101(101, "1.0.1"),
    V102(102, "1.0.2"),
    V103(103, "1.0.3"),
    V2(200, "2.0.0"),
    V210(210, "2.1.0"),
    V211(211, "2.1.1"),
    V212(212, "2.1.2");

    int code;
    String value;

    VersionEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

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
    public static VersionEnum get(String userAgent) {
        if (StringUtils.isEmpty(userAgent)) {
            return null;
        }
        // 避免出现给过来的是以 Mozilla/5.0 开头, 结果最后被匹配成 5.0 的版本了
        if (userAgent.startsWith("LYLawyerPlatform")) {
            // LYLawyerPlatform/1.0.1  LYLawyerPlatform_Lawyer/1.0.2
            String[] typeAndVersion = userAgent.split(" ")[0].split("/");
            if (typeAndVersion.length == 2) {
                for (VersionEnum versionEnum : VersionEnum.values()) {
                    if(versionEnum.getValue().equals(typeAndVersion[1].trim())){
                        return  versionEnum;
                    }
                }
            }
        }
        return null;
    }

    public static VersionEnum current() {
        return V212;
    }
}