package com.hhly.web.service;

import com.google.gson.Gson;

import com.hhly.api.dto.ResultObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
* @author wangxianchen
* @create 2017-09-05
* @desc 基于RestTemplate的服务的实现
*/
public abstract class AbstractBaseRestService implements BaseRestService {

    @Autowired
    protected RestTemplate restTemplate;

    final Gson gson = new Gson();

    /**
     * @desc 构建URL,由子类实现
     * @author wangxianchen
     * @create 2017-09-05
     * @param url
     * @return
     */
    public abstract String buildURL(String url);

    /**
     * @desc 格式化URL,拼装参数
     * @author wangxianchen
     * @create 2017-09-14
     * @param url
     * @param params
     * @return
     */
    public String formatURL(String url,Map<String,?> params){
        Set<String> set = params != null ? params.keySet() : null;
        StringBuilder sb = new StringBuilder(buildURL(url));
        if(set != null && set.size() != 0 ){
            Iterator<String> it = set.iterator();
            sb.append("?");
            while (it.hasNext()){
                String key = it.next();
                sb.append(key);
                sb.append("={");
                sb.append(key);
                sb.append("}");
                if(it.hasNext()){
                    sb.append("&");
                }
            }
        }
        return sb.toString();
    }

    /**
     * @desc 返回ResultObject的默认实现
     * @author wangxianchen
     * @create 2017-09-05
     * @param url
     * @param httpEntity
     * @return
     */
    public ResultObject send(String url, HttpEntity<String> httpEntity){
        String json = this.post(url,httpEntity);
        return gson.fromJson(json,ResultObject.class);
    }

    /**
     * @desc 返回ResultObject的默认实现
     * @author wangxianchen
     * @create 2017-09-05
     * @param url
     * @param httpEntity
     * @param params
     * @return
     */
    public ResultObject send(String url, HttpEntity<String> httpEntity,Map<String,?> params){
        String json = this.get(url,httpEntity,params);
        return gson.fromJson(json,ResultObject.class);
    }

    /**
     * @desc POST提交方式实现
     * @author wangxianchen
     * @create 2017-09-05
     * @param url
     * @param httpEntity
     * @return
     */
    @Override
    public <T> T post(String url, HttpEntity<?> httpEntity,Class<T> clazz){
        return restTemplate.postForObject(buildURL(url), httpEntity, clazz);
    }

    /**
     * @desc POST方式提交,返回字符串
     * @author wangxianchen
     * @create 2017-09-05
     * @param url
     * @param httpEntity
     * @return
     */
    @Override
    public String post(String url, HttpEntity<?> httpEntity){
        return restTemplate.postForObject(buildURL(url), httpEntity, String.class);
    }

    /**
     * @desc GET提交方式实现
     * @author zhangshuqing
     * @create 2017-09-05
     * @param url
     * @param httpEntity
     * @param params
     * @return
     */
    @Override
    public String get(String url, HttpEntity<?> httpEntity, Map<String,?> params){
        ResponseEntity<String> response = restTemplate.exchange(formatURL(url,params), HttpMethod.GET, httpEntity, String.class, params);
        return response.getBody();
    }

    /**
     * @desc get方式提交,返回指定对象
     * @author wangxianchen
     * @create 2017-09-05
     * @param url
     * @param httpEntity
     * @param params
     * @param clazz
     * @return
     */
    @Override
    public <T> T get(String url, HttpEntity<?> httpEntity,Map<String,?> params,Class<T> clazz){
        ResponseEntity<T> response = restTemplate.exchange(formatURL(url,params), HttpMethod.GET, httpEntity, clazz, params);
        return response.getBody();
    }

    /**
     * @desc PUT提交方式实现
     * @author pengchao
     * @create 2017-09-18
     * @param url
     * @param httpEntity
     * @param params
     * @return
     */
    @Override
    public String put(String url, HttpEntity<?> httpEntity, Map<String,?> params){
        ResponseEntity<String> response = restTemplate.exchange(formatURL(url,params), HttpMethod.PUT, httpEntity, String.class, params);
        return response.getBody();
    }

    /**
     * @desc PUT提交方式实现
     * @author pengchao
     * @create 2017-09-18
     * @param url
     * @param httpEntity
     * @param params
     * @param clazz
     * @return
     */
    @Override
    public <T> T put(String url, HttpEntity<?> httpEntity,Map<String,?> params,Class<T> clazz){
        ResponseEntity<T> response = restTemplate.exchange(formatURL(url,params), HttpMethod.PUT, httpEntity, clazz, params);
        return response.getBody();
    }

    /**
     * @desc PUT提交方式实现
     * @author pengchao
     * @create 2017-09-18
     * @param url
     * @param httpEntity
     * @param params
     * @return
     */
    @Override
    public String delete(String url, HttpEntity<?> httpEntity, Map<String,?> params){
        ResponseEntity<String> response = restTemplate.exchange(formatURL(url,params), HttpMethod.DELETE, httpEntity, String.class, params);
        return response.getBody();
    }

    /**
     * @desc PUT提交方式实现
     * @author pengchao
     * @create 2017-09-18
     * @param url
     * @param httpEntity
     * @param params
     * @param clazz
     * @return
     */
    @Override
    public <T> T delete(String url, HttpEntity<?> httpEntity,Map<String,?> params,Class<T> clazz){
        ResponseEntity<T> response = restTemplate.exchange(formatURL(url,params), HttpMethod.DELETE, httpEntity, clazz, params);
        return response.getBody();
    }
}
