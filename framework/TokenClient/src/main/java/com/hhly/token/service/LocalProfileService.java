package com.hhly.token.service;

import com.hhly.token.constant.Constant;
import com.hhly.token.encrypt.MD5;
import com.hhly.token.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author pengchao
 * @create 2017-12-06
 * @desc 访问者基本信息(token)
 */
public class LocalProfileService {
    private static final Logger logger = LoggerFactory.getLogger("token");

    public static UserInfo buildUserInfo(Map<String, Object> attributes) {
        UserInfo userInfo = null;
        if (attributes != null) {
            userInfo = new UserInfo();
            userInfo.setNickname(getValue(attributes, Constant.PROFILE_ATTRIBUTE_KEY_USERNAME));
            userInfo.setId(getValue(attributes, Constant.PROFILE_ATTRIBUTE_KEY_USERID));
            userInfo.setUsername(getValue(attributes, Constant.PROFILE_ATTRIBUTE_KEY_ACCOUNT));
            userInfo.setCellphone(getValue(attributes, Constant.PROFILE_ATTRIBUTE_KEY_CELLPHONE));
            userInfo.setSessionkey(getValue(attributes, Constant.PROFILE_ATTRIBUTE_KEY_SESSION));
        }
        return userInfo;
    }

    public static String getUserid(Map<String, Object> attributes) {
        return getValue(attributes, Constant.PROFILE_ATTRIBUTE_KEY_USERID);
    }

    public static String getValue(Map<String, Object> attributes, String key) {
        String result = "";
        Object tempObj = attributes.get(key);
        if (tempObj != null) {
            result = tempObj.toString();
        }
        return result;
    }

    public static String calculateSID(String userId) {
        String result = null;
        if (userId != null) {
            result = MD5.encode(userId).toLowerCase();
        }
        return result;
    }

    public static String wrapToken(String jwt, String sid) {
        return jwt + "-" + sid;
    }

    public static String[] extractToken(String token) {
        String result[] = new String[2];

        if (token != null) {
            try {
                int pos = token.lastIndexOf("-");
                if (pos != -1) {
                    String sid = token.substring(pos + 1, token.length());
                    if (sid.length() == 32) {
                        result[0] = sid;
                        result[1] = token.substring(0, pos);
                    } else {
                        logger.error("sid长度不足！[token = {}]", token);
                    }
                }
            } catch (Exception e) {
                logger.error("token解析出错！[token = {}]", token);
            }
        }

        return result;
    }
}