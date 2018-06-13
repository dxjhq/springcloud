package com.hhly.api.model;

import com.hhly.api.enums.AppUserTypeEnum;
import com.hhly.api.enums.OsTypeEnum;
import com.hhly.api.enums.VersionEnum;
import org.apache.commons.lang3.StringUtils;
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
public class AppClient {

    OsTypeEnum osType;
    AppUserTypeEnum userType;
    VersionEnum version;
    private AppClient() {}

    public static AppClient get(String userAgent, String appParam) {
        AppClient client = new AppClient();
        if (!StringUtils.isEmpty(userAgent)) {
            client.osType = OsTypeEnum.get(userAgent);
            client.userType = AppUserTypeEnum.get(userAgent);
            client.version = VersionEnum.get(userAgent);

            // 如果 user-agent 中没有版本信息, 从 header(param) 再查一下
            if (client.version == null) {
                for (VersionEnum versionEnum : VersionEnum.values()) {
                    if(versionEnum.getValue().equals(appParam)){
                        client.version = versionEnum;
                    }
                }
            }
        }
        return client;
    }

    public VersionEnum getVersion() {
        return version;
    }
    public Integer getAppVersion() {
        return version != null ? version.getCode(): null;
    }
    public String getAppUserType() {
        return (userType != null ? userType : AppUserTypeEnum.User).getValue();
    }
    public String getAppOsTypeWithStr() {
        return (osType != null ? osType : OsTypeEnum.Other).name();
    }
    public Integer getAppOsType() {
        return (osType != null ? osType : OsTypeEnum.Other).getCode();
    }
}
