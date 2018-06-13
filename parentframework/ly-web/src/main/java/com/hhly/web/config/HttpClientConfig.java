package com.hhly.web.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wangxianchen
 * @create 2018-03-09
 * @desc httpClient超时配置
 */

@ConfigurationProperties(prefix = "httpClient")
@Getter
@Setter
public class HttpClientConfig {

    /**
    * @author wangxianchen
    * @create 2018-03-09
    * @desc 从底层的HttpClient链接管理中获取链接请求的超时设置，单位：毫秒,默认60秒
     * Set the timeout in milliseconds used when requesting a connection from the connection manager using the underlying HttpClient.
    */
    private int connectionRequestTimeout = 60000;

    /**
    * @author wangxianchen
    * @create 2018-03-09
    * @desc 为底层的HttpClient获取链接，设置超时时间，单位：毫秒,默认60秒
     * Set the connection timeout for the underlying HttpClient.
    */
    private int connectTimeout = 60000;

    /**
    * @author wangxianchen
    * @create 2018-03-09
    * @desc 为底层的HttpClient socket读取，设置超时时间，单位：毫秒,默认60秒
     * Set the socket read timeout for the underlying HttpClient.
    */
    private int readTimeout = 60000;
}
