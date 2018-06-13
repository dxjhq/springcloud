package com.hhly.web.web;

import com.hhly.utils.web.CookieUtil;
import com.hhly.utils.web.HttpHelper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
* @author wangxianchen
* @create 2017-09-05
* @desc base controller
*/
public class BaseController {

    /**
     * @desc 获取token
     * @author wangxianchen
     * @create 2018-01-30
     * @param request
     * @return
     */
    public String getToken(HttpServletRequest request){
        return CookieUtil.getToken(request);
    }

    /**
     * @desc 获取request里的所有header
     * @author wangxianchen
     * @create 2018-01-30
     * @param request
     * @return
     */
    public HttpHeaders getHeaders(HttpServletRequest request){
        HttpHeaders headers = new HttpHeaders();
        String method=request.getMethod();
        if(method.equalsIgnoreCase(HttpMethod.POST.toString())){ //所有的post 请求都是json
            headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        }
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        Enumeration<String> enmus = request.getHeaderNames();
        while(enmus.hasMoreElements()){
            String name = enmus.nextElement();
            headers.add(name,request.getHeader(name));
        }
        return headers;
    }

    /**
     * @desc 获取request里的所有header
     * @author wangxianchen
     * @create 2018-01-30
     * @return
     */
    public HttpHeaders getHeaders(){
        return getHeaders(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
    }

    /**
     * @desc 获取请求实体
     * @author wangxianchen
     * @create 2018-01-30
     * @param request
     * @return
     * @throws Exception
     */
    public HttpEntity<String> getHttpEntity(HttpServletRequest request) throws Exception {
        return new HttpEntity<String>(HttpHelper.getRequestPostStr(request),this.getHeaders(request));
    }

    /**
     * @desc 获取请求实体
     * @author wangxianchen
     * @create 2018-01-30
     * @return
     * @throws Exception
     */
    public HttpEntity<String> getHttpEntity() throws Exception {
        return getHttpEntity(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
    }

}
