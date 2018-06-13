package com.hhly.im.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author wangxianchen
 * @create 2017-10-23
 * @desc 手机短信之业务属性配置
 */
//@Component
//@ConfigurationProperties(prefix = "imSmsConfigProperties")
@Getter
@Setter
public class SmsConfigProperties {

    /**
     * key=业务类型
     * value=短信发送间隔时间,单位:秒
     */
    private Map<String,Integer> bizMap;

}
