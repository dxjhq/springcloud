package com.hhly.elasticsearch.config;

import com.hhly.elasticsearch.component.TransportClientFactory;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangxianchen
 * @create 2017-09-08
 * @desc ES配置
 */
@Configuration
public class ElasticSearchConfig{

    @Bean
    public TransportClient client(TransportClientFactory transportClientFactory) throws Exception {
        return transportClientFactory.getObject();
    }


}
