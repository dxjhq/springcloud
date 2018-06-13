package com.hhlz.gateway.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hhly-pc on 2017/8/21.
 */
public class BlackFilter implements Filter {

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(request, response);
        //returnJson(response,request,"sssss");
    }

    private void returnJson(ServletResponse servletResponse, ServletRequest servletRequest, String json) {
        try {
            HttpServletRequest request = (HttpServletRequest)servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;

            String contentType = "application/json; charset=UTF-8";
            if (request != null) {
                String accept = request.getHeader("accept");
                if (accept != null && !accept.contains("json")) {
                    contentType = "text/html; charset=UTF-8";
                }
            }
            response.setContentType(contentType);
            response.getWriter().write(json);
            response.getWriter().flush();
        } catch (IOException e) {
        }
    }
    @Override
    public void init(FilterConfig arg0) throws ServletException {
        String xx ="";
    }

}
