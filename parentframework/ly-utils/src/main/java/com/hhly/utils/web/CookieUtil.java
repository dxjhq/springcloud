package com.hhly.utils.web;

import com.hhly.api.constant.HttpConst;
import com.hhly.api.constant.TimeConst;
import com.hhly.utils.LogUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
* @author wangxianchen
* @create 2017-09-01
* @desc cookie工具类
*/
public class CookieUtil {
    /**
     * @desc 删除cookie
     * @author wangxianchen
     * @create 2017-09-01
     * @param request
     * @param response
     * @param name
     */
    public static void delCookie(HttpServletRequest request, HttpServletResponse response, String name){
        Cookie[] cookies = request.getCookies();
        if (null==cookies) {
            LogUtil.ROOT_LOG.warn("没有cookie====");
        } else {
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(name)){
                    cookie.setValue(null);
                    cookie.setMaxAge(0);// 立即销毁cookie
                    cookie.setPath("/");
                    LogUtil.ROOT_LOG.warn("被删除的cookie名字为:"+cookie.getName());
                    response.addCookie(cookie);
                    break;
                }
            }
        }
    }

    /**
     * @desc 设置cookie
     * @author wangxianchen
     * @create 2017-09-01
     * @param response
     * @param token
     */
    public static void setCookie(HttpServletResponse response,String token){
        Cookie cookie = new Cookie(HttpConst.REQUEST_HEADER_KEY_TOKEN,token);
        cookie.setMaxAge((int)TimeConst.TIME_OUT_TWO_HOUR);
        cookie.setPath("/");
        //cookie.setDomain("");
        response.addCookie(cookie);
    }

    /**
     * @desc 读取所有cookie
     * @author wangxianchen
     * @create 2017-09-01
     * @param request
     * @return
     */
    private static Map<String,Cookie> readCookieMap(HttpServletRequest request){
        Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
        Cookie[] cookies = request.getCookies();
        if(null != cookies){
            for(Cookie cookie : cookies){
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }

    /**
     * @desc 根据cookie名字获取值
     * @author wangxianchen
     * @create 2017-09-01
     * @param request
     * @param name
     * @return
     */
    public static Cookie getCookieByName(HttpServletRequest request,String name){
        Map<String,Cookie> cookieMap = readCookieMap(request);
        if(cookieMap.containsKey(name)){
            Cookie cookie = (Cookie)cookieMap.get(name);
            return cookie;
        }else{
            return null;
        }
    }

    /**
     * @desc 获取token
     * @author wangxianchen
     * @create 2017-09-01
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request){
        return request.getHeader(HttpConst.REQUEST_HEADER_KEY_TOKEN);
    }
}