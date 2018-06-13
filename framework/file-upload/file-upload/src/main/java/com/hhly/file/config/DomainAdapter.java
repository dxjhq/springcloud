package com.hhly.file.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 解决跨域问题 适配器
 * @author BSW
 * @create 2017-10-11
 * @desc
 */
public class DomainAdapter extends WebMvcConfigurerAdapter {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                System.out.println("domainAdapter initer ....>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
            }
        };
    }

}
