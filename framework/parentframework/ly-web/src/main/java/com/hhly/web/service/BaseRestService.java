package com.hhly.web.service;

import org.springframework.http.HttpEntity;
import java.util.Map;

/**
* @author wangxianchen
* @create 2017-09-05
* @desc 基于RestTemplate的服务
*/
public interface BaseRestService{

    /**
     * @desc POST方式提交,返回指定对象
     * @author wangxianchen
     * @create 2017-09-05
     * @param url
     * @param httpEntity
     * @param clazz
     * @return
     */
    <T> T post(String url, HttpEntity<?> httpEntity, Class<T> clazz);

    /**
     * @desc POST方式提交,返回字符串
     * @author wangxianchen
     * @create 2017-09-05
     * @param url
     * @param httpEntity
     * @return
     */
    String post(String url, HttpEntity<?> httpEntity);


    /**
     * @desc GET方式提交,返回对象
     * @author wangxianchen
     * @create 2017-09-05
     * @param url
     * @param httpEntity
     * @param params
     * @param clazz
     * @return
     */
    <T> T get(String url, HttpEntity<?> httpEntity,Map<String,?> params,Class<T> clazz);

    /**
     * @desc GET方式提交,返回字符串
     * @author zhangshuqing
     * @create 2017-09-05
     * @param url
     * @param httpEntity
     * @param params
     * @return
     */
    String get(String url, HttpEntity<?> httpEntity,Map<String,?> params);

    /**
     * @desc Put,返回对象
     * @author pengchao
     * @create 2017-09-18
     * @param url
     * @param httpEntity
     * @param params
     * @param clazz
     * @return
     */
    <T> T put(String url, HttpEntity<?> httpEntity,Map<String,?> params,Class<T> clazz);

    /**
     * @desc Put方式提交,返回字符串
     * @author pengchao
     * @create 2017-09-18
     * @param url
     * @param httpEntity
     * @param params
     * @return
     */
    String put(String url, HttpEntity<?> httpEntity,Map<String,?> params);

    /**
     * @desc Delete,返回对象
     * @author pengchao
     * @create 2017-09-18
     * @param url
     * @param params
     * @return
     */
    <T> T delete(String url, HttpEntity<?> httpEntity,Map<String,?> params,Class<T> clazz);

    /**
     * @desc Delete,返回字符串
     * @author pengchao
     * @create 2017-09-18
     * @param url
     * @return
     */
    String delete(String url, HttpEntity<?> httpEntity,Map<String,?> params);
}