package com.hhly.businesscomm.ice;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "ice")
public class IceProperties {

    private Info user;
    private Info pay;

    @Setter
    @Getter
    public static class Info {
        private String cfgPath;
        private String ip;
        private String port;
        private String type;
        private Integer timeOut;
    }
}
