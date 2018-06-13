package com.hhly.jdbc.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.miemiedev.mybatis.paginator.OffsetLimitInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import javax.sql.DataSource;

/**
* @author wangxianchen
* @create 2017-07-21
* @desc mybatis配置
*/
public class MybatisConfig {
    
    private Logger logger = LoggerFactory.getLogger(MybatisConfig.class);

    private final String configLocation = "classpath:mybatis-config.xml";

    private final String mapperLocations = "mapper/**/*Mapper*.xml";

    private final String dialectClass = "com.github.miemiedev.mybatis.paginator.dialect.MySQLDialect";

	@Bean(name="dataSource")
	@ConfigurationProperties(prefix = "spring.datasource.druid")
	@Primary
	public DataSource dataSource() {
		logger.info("master com.alibaba.druid.pool.DruidDataSource will be create.....");
		return DataSourceBuilder.create().type(DruidDataSource.class).build();
	}

    @Bean(name = "sqlSessionFactory")
	@Primary
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) {
        logger.info("initialization sqlSessionFactory...");
        try {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(dataSource);
            //设置别名适配器目录,在mybatis-config里指定
            //bean.setTypeAliasesPackage(typeAliasesPackage);
            //设置类型转换器目录,在mybatis-config里指定
            //bean.setTypeHandlersPackage(typeHandlersPackage);
            OffsetLimitInterceptor interceptor = new OffsetLimitInterceptor();
            interceptor.setDialectClass(dialectClass);
            //设置方言插件
            bean.setPlugins(new Interceptor[]{interceptor});
            //指定mapper文件目录
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            bean.setMapperLocations(resolver.getResources(mapperLocations));
            //指定config文件位置
            //Resource fileResource = new ClassPathResource(configLocation.replace("classpath:",""));
            Resource fileResource = new DefaultResourceLoader().getResource(configLocation);
            if(fileResource.exists()){
                bean.setConfigLocation(fileResource);
            }else{
                logger.warn(configLocation+"不存在");
            }
            return bean.getObject();
        } catch (Exception e) {
        	logger.error("创建SqlSessionFactory失败");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean
	@Primary
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        logger.info("initialization sqlSessionTemplate...");
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name="transactionManager")
	@Primary
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        logger.info("initialization transactionManager...");
        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(dataSource);
        return manager;
    }

}