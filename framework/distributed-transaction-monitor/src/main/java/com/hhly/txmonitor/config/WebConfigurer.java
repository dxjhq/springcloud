package com.hhly.txmonitor.config;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author wangxianchen
 * @create 2017-10-26
 * @desc
 */
//@Configuration
public class WebConfigurer extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers( ViewControllerRegistry registry ) {
        registry.addViewController( "/" ).setViewName( "forward:/monitor/index" );
        registry.setOrder( Ordered.HIGHEST_PRECEDENCE );
       super.addViewControllers( registry );
    }
}
