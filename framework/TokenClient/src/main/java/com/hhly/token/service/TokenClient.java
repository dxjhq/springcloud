package com.hhly.token.service;

import com.hhly.token.constant.Constant;
import com.hhly.token.encrypt.Encrypt;
import com.hhly.token.encrypt.MD5;
import com.hhly.token.model.LocalProfile;
import com.hhly.token.model.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pengchao
 * @create 2017-12-14
 * @desc
 */
public class TokenClient {
    /**
     * Token算法
     * @param localProfile
     * @return
     */
    public static LocalProfile calculateToken(LocalProfile localProfile) {
        LocalProfile myLocalProfile = new LocalProfile();
        //本地账户信息不为空
        if (localProfile != null) {
            Map<String, Object> profileMap = new HashMap<String, Object>();
            profileMap.put(Constant.PROFILE_ATTRIBUTE_KEY_ACCOUNT, localProfile.getAccount());
            profileMap.put(Constant.PROFILE_ATTRIBUTE_KEY_USERID, localProfile.getUserid());
            profileMap.put(Constant.PROFILE_ATTRIBUTE_KEY_USERNAME, localProfile.getUsername());
            profileMap.put(Constant.PROFILE_ATTRIBUTE_KEY_CELLPHONE, localProfile.getCellphone());
            profileMap.put(Constant.PROFILE_ATTRIBUTE_KEY_NANOTIME, localProfile.getNanoTime());
            profileMap.put(Constant.PROFILE_ATTRIBUTE_KEY_SESSION, localProfile.getUserkey());
            //profileMap.put(Constant.PROFILE_ATTRIBUTE_KEY_CHECKSUM, localProfile.getCheckSum());

            if (localProfile.getTimeout() == 0) {
                localProfile.setTimeout(Constant.TIME_OUT_TWO_HOUR);
            }
            localProfile.setExpireTime(calculateExpire(localProfile.getTimeout()));

            //String sid = LocalProfileAttribute.calculateSID(localProfile.getUserid()+":" +localProfile.getUserkey());
            String sid = LocalProfileService.calculateSID(localProfile.getUserid() + ":" + localProfile.getAccount());
            LocalProfile oldProfile = TokenCache.get(sid);

            //存在旧版本地账户信息
            if (oldProfile != null) {
                oldProfile.setExpireTime(localProfile.getExpireTime());
                oldProfile.setTimeout(localProfile.getTimeout());
                myLocalProfile = oldProfile;
            } else {
                localProfile.setJwt(Encrypt.jwtEncode(profileMap));
                localProfile.setSid(sid);
                localProfile.setCheckSum(MD5.encode(localProfile.getJwt()));
                myLocalProfile = localProfile;
            }
            //混淆token
            //myLocalProfile.setJwt(LocalProfileService.wrapToken(myLocalProfile.getJwt(), sid));
            //TokenCache.set(myLocalProfile, myLocalProfile.getTimeout());
        }
        return myLocalProfile;
    }

    /**
     * token解密
     * @param token
     * @return
     */
    public static Map<String, Object> getProfileMap(String token){
        String[] tokenInfos = LocalProfileService.extractToken(token);
        if (StringUtils.isEmpty(tokenInfos[0])) {
            return null;
        }
        Map<String, Object>  profileMap = Encrypt.jwtDecode(tokenInfos[1]);
        profileMap.put(Constant.PROFILE_ATTRIBUTE_KEY_SID,tokenInfos[0]);
        return profileMap;
    }

    /**
     * 获取账号信息
     * @param token
     * @return
     */
    public static LocalProfile getProfile(String token){
        String[] tokenInfos = LocalProfileService.extractToken(token);
        if (StringUtils.isEmpty(tokenInfos[0])) {
            return null;
        }
        return TokenCache.get(tokenInfos[0]);
    }

    /**
     * 获取原始token
     * @param token
     * @return
     */
    public static String getOriginalJwt(String token) {
        String[] strArray = LocalProfileService.extractToken(token);
        String sid = strArray[0];
        String originalJwt = strArray[1];
        if (StringUtils.isEmpty(sid)) {
            return "";
        }
        return originalJwt;
    }

    /**
     * Token验证
     * @param token
     * @return
     */
    public static boolean validate(String token) {
        LocalProfile localProfile = getProfile(token);
        if (localProfile == null || StringUtils.isEmpty(localProfile.getJwt())) {
            return false;
        }
        return validate(token,localProfile);
    }
    /**
     * Token验证
     * @param token
     * @param localProfile
     * @return
     */
    public static boolean validate(String token,LocalProfile localProfile) {
        return LocalProfileService.wrapToken(localProfile.getJwt(),localProfile.getSid()).equals(token);
    }

    /**
     * 刷新token时间
     * @param token
     * @param timeout
     * @return
     */
    public static boolean refreshExpire(String token,Long timeout){
        String[] tokenInfos = LocalProfileService.extractToken(token);
        if (StringUtils.isEmpty(tokenInfos[0])) {
            return false;
        }
        if(timeout <= 0){
            timeout = Constant.TIME_OUT_TWO_HOUR;
        }
        return TokenCache.resetExpire(tokenInfos[0],timeout);
    }
    /**
     * 刷新token时间
     * @param localProfile
     * @return
     */
    public static boolean refreshExpire(LocalProfile localProfile){
        if (localProfile == null || StringUtils.isEmpty(localProfile.getSid())) {
            return false;
        }
        if(localProfile.getTimeout() <= 0){
            localProfile.setTimeout(Constant.TIME_OUT_TWO_HOUR);
        }
        //localProfile.setExpireTime(calculateExpire(localProfile.getTimeout()));
        return TokenCache.resetExpire(localProfile.getSid(),localProfile.getTimeout());
    }

    /**
     * 写入token
     * @param localProfile
     */
    public static void setToken(LocalProfile localProfile){
        TokenCache.set(localProfile);
    }

    /**
     * 删除token
     * @param token
     */
    public static void delToken(String token){
        String[] tokenInfos = LocalProfileService.extractToken(token);
        if (StringUtils.isEmpty(tokenInfos[0])) {
            return;
        }
        TokenCache.delete(tokenInfos[0]);
    }

    /**
     * 删除token
     * @param localProfile
     */
    public static void delToken(LocalProfile localProfile){
        if(localProfile == null || StringUtils.isEmpty(localProfile.getSid())){
            return;
        }
        TokenCache.delete(localProfile.getSid());
    }

    /**
     * 获取当前UserId
     * @return
     */
    public static String getCurrentUserId() {
        return getCurrentUserId(getCurrentRequest());
    }
    public static String getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader(Constant.REQUEST_HEADER_KEY_TOKEN);
        if(StringUtils.isEmpty(token)){
            return "";
        }
        return getCurrentUserId(token);
    }
    public static String getCurrentUserId(String token){
        String originalJwt= TokenClient.getOriginalJwt(token);
        if(StringUtils.isEmpty(originalJwt)){
            return "";
        }
        Map<String, Object> profile = Encrypt.jwtDecode(originalJwt);
        return profile.get(Constant.PROFILE_ATTRIBUTE_KEY_USERID).toString();
    }

    /**
     * 获取当前UserInfo
     * @return
     */
    public static UserInfo getCurrentUser() {
        return getCurrentUser(getCurrentRequest());
    }
    public static UserInfo getCurrentUser(HttpServletRequest request) {
        String token = request.getHeader(Constant.REQUEST_HEADER_KEY_TOKEN);
        if(StringUtils.isEmpty(token)){
            return new UserInfo();
        }
        return getCurrentUser(token);
    }
    public static UserInfo getCurrentUser(String token){
        String originalJwt= TokenClient.getOriginalJwt(token);
        if(StringUtils.isEmpty(originalJwt)){
            return new UserInfo();
        }
        Map<String, Object> profile = Encrypt.jwtDecode(originalJwt);
        return LocalProfileService.buildUserInfo(profile);
    }

    /**
     * 当前访问Request
     * @return
     */
    private static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 计算过期时间，timeout单位是秒
     * @param timeout
     * @return 返回单位是毫秒
     */
    private static Long calculateExpire(Long timeout) {
        return System.currentTimeMillis() + timeout * 1000;
    }
}