package com.hhly.tracker.interceptor;

import com.hhly.tracker.constant.Const;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author pengchao
 * @create 2017-11-24
 * @desc 访问拦截器
 */
public class RequestInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        //todo: 每次访问+1，压测用
        //Const.REQUEST_COUNT.addAndGet(1);
        //System.out.println(httpServletRequest.getRequestURI() + ":" + Const.REQUEST_COUNT.toString());
        //return true;

        return super.preHandle(httpServletRequest,httpServletResponse,handler);
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(httpServletRequest, httpServletResponse, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception ex) throws Exception {
        super.afterCompletion(httpServletRequest, httpServletResponse, handler, ex);
    }
}