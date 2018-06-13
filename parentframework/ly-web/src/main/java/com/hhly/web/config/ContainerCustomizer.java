package com.hhly.web.config;

import com.hhly.utils.web.IPUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.stereotype.Component;

/**
 * @author pengchao
 * @create 2018-01-25
 * @desc
 */
@Component
public class ContainerCustomizer implements EmbeddedServletContainerCustomizer {
    @Value("${server.port:8080}")
    private int servicePort;

    @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
         while (servicePort<65535){
            if(IPUtil.isPortAvailable(servicePort)){
                break;
            }
            servicePort++;
        }
        configurableEmbeddedServletContainer.setPort(servicePort);
    }
}
