package com.hhly.txmonitor;



import com.hhly.tx.componet.PublishConfig;
import com.hhly.tx.componet.TxAspect;
import com.hhly.web.config.BaseConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {
        TxAspect.class,
        PublishConfig.class
})
@EnableDiscoveryClient
@Import({
        BaseConfiguration.class,
})
public class WebApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(WebApplication.class, args);
    }



}

