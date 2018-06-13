package com.hhly.web.config;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

import com.hhly.api.constant.PageConst;
import com.hhly.web.annotations.ApiVersion;
import com.hhly.web.annotations.ApiVersionCondition;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.List;

/**
* @author pengchao
* @create 2017-09-13
* @desc 初始化Bean、添加自定义拦截器等
*/
@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {
    /**
     * 跨域
     * @return
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
            }
        };
    }


    /**
     * 分页参数赋初始值
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        // 如果 Controller 方法中的参数有 PageBounds 则从前台获取数据组装, 如果没有传递则给设置一个默认值
        argumentResolvers.add(new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return PageBounds.class.isAssignableFrom(parameter.getParameterType());
            }
            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                          NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
                // 判断数据是否合理, 不合理就给定默认值
                int page = NumberUtils.toInt(request.getParameter(PageConst.GLOBAL_PAGE));
                if (page <= 0) page = PageConst.DEFAULT_PAGE_NO;
                int limit = NumberUtils.toInt(request.getParameter(PageConst.GLOBAL_LIMIT));
                if (limit <= 0) limit = PageConst.DEFAULT_LIMIT;
                return new PageBounds(page, limit);
            }
        });
    }

    /**
     * Api版本兼容
     * @return
     */
    @Override
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping() {
            /** 在类上标注了 ApiVersion 时 */
            @Override
            protected RequestCondition<?> getCustomTypeCondition(
                    Class<?> handlerType) {
                ApiVersion apiVersion = AnnotationUtils
                        .findAnnotation(handlerType, ApiVersion.class);
                return apiVersion == null ? null
                        : new ApiVersionCondition(apiVersion.value());
            }

            /** 在方法上标注了 ApiVersion 时 */
            @Override
            protected RequestCondition<?> getCustomMethodCondition(
                    Method method) {
                ApiVersion apiVersion = AnnotationUtils.findAnnotation(method,
                        ApiVersion.class);
                return apiVersion == null ? null
                        : new ApiVersionCondition(apiVersion.value());
            }
        };
    }
}