package com.hhly.api.enums;

import com.hhly.api.constant.RegexConst;
import org.apache.commons.lang3.StringUtils;
import java.util.regex.Pattern;

/**
 * app 访问来源(1.pc, 2.iOS, 3.安卓, 4.其他)
 */
public enum OsTypeEnum {
    // 对应的 1 2 3 4 的值是会员中心定义的
    PC(1),
    IOS(2),
    Android(3),
    Other(4);

    int code;

    OsTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static OsTypeEnum get(String userAgent) {
        if (verification(userAgent, RegexConst.IOS)) {
            return OsTypeEnum.IOS;
        }
        if (verification(userAgent, RegexConst.ANDROID)) {
            return OsTypeEnum.Android;
        }
        if (verification(userAgent, RegexConst.PC)) {
            return OsTypeEnum.PC;
        }
        return Other;
    }

    private static boolean verification(String value, String regex) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        return Pattern.compile(regex).matcher(value).find();
    }
}