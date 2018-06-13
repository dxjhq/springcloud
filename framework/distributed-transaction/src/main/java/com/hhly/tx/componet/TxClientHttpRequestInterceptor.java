package com.hhly.tx.componet;

import com.hhly.tx.constant.TxConstant;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;

/**
 * @author wangxianchen
 * @create 2017-11-16
 * @desc resttemplate拦截器,用于添加requestHeader参数
 */
public class TxClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
        requestWrapper.getHeaders().add(TxConstant.TX_MSG_ID, TxAspect.getLocalMsgId());
        return execution.execute(request, body);
    }
}
