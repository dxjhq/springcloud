package com.hhly.hbase.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.apache.hadoop.security.UserGroupInformation;

import java.util.logging.Logger;

/** 项目中需要额外加载的类 */
@Configuration
public class HbaseBeanInit {
    private static final Logger log = Logger.getLogger(HbaseBeanInit.class.getName());

    @Value("${hbase.master}")
    private String hbaseMaster;

    @Value("${hbase.zookeeper.quorum}")
    private String hbaseQuorum;


    /** Hbase认证码 **/
    @Value("${hbase.authen.code1}")
    private String authCode;

    /**
     * 实例化 RedisTemplate 对象
     * @return
     */
    @Bean
    public HbaseTemplate functionHbaseTemplateInit() {
        //创建连接前，先设置认证信息，认证码由大数据平台(位文超)提供认证码
        UserGroupInformation.setLoginUser(UserGroupInformation.createRemoteUser(authCode));
        HbaseTemplate hbaseTemplate = new HbaseTemplate();
        org.apache.hadoop.conf.Configuration configuration = new org.apache.hadoop.conf.Configuration();
        configuration.set("hbase.master",hbaseMaster);
        configuration.set("hbase.zookeeper.quorum",hbaseQuorum);
        log.info("初始化hbaseConfiguration :"+ configuration.get("hbase.master") + "-->"+ configuration.get("hbase.zookeeper.quorum"));
        hbaseTemplate.setConfiguration(configuration);
        log.info("hbaseTemplate Object is:"+hbaseTemplate.toString());
        return hbaseTemplate;
    }


}
