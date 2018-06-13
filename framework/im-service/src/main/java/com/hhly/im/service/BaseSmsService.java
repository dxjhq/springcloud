package com.hhly.im.service;

import java.util.LinkedList;

/**
 * @author wangxianchen
 * @create 2017-10-24
 * @desc 手机短信服务
 */
public interface BaseSmsService {

    void sendTemplateSms(String mobile, String tempId, LinkedList<String> msgContentParams,String redisKey);

    void sendSms(String mobile, String content,String redisKey);
}
