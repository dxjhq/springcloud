package com.hhly.jdbc.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

/**
 * @author wangxianchen
 * @create 2017-11-01
 * @desc 2017-11-02 改为自动配置
 */
public class MybatisMapperScannerConfig {

    private static Logger logger = LoggerFactory.getLogger(MybatisMapperScannerConfig.class);
    
    private static final String basePackages = "com.hhly.**.mapper";

    @Bean
    public static MapperScannerConfigurer mapperScannerConfigurer() {
        logger.info("initialization MapperScannerConfigurer. basePackages={}",basePackages);
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(basePackages);
        return mapperScannerConfigurer;
    }
}
