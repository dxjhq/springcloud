package com.hhly.im.service;

import com.hhly.api.exception.ValidationException;
import com.hhly.cache.service.RedisService;
import com.hhly.im.config.SmsConfigProperties;
import com.hhly.im.constant.ImConstant;
import com.hhly.utils.SendSmsUtil;
import com.hhly.utils.ValueUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.LinkedList;

/**
 * @author wangxianchen
 * @create 2017-10-24
 * @desc 手机短信服务默认实现
 */
public abstract class AbstractSmsService implements BaseSmsService {
    
    private Logger logger = LoggerFactory.getLogger(AbstractSmsService.class);
            
    @Autowired
    RedisService redisService;

    @Resource(name = "ImThreadPoolTaskExecutor")
    protected ThreadPoolTaskExecutor taskExecutor;

    @Autowired(required = false)
    protected SmsConfigProperties smsConfigProperties;

    @Value("${spring.application.name}")
    protected String applicationName;

    /**
     * @desc 根据模板发送短信
     * @author wangxianchen
     * @create 2017-10-25
     * @param mobile
     * @param tempId
     * @param msgContentParams
     * @param redisKey 可以为null
     */
    @Override
    public void sendTemplateSms(final String mobile, final String tempId, final LinkedList<String> msgContentParams,String redisKey){
        try {
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String result = SendSmsUtil.sendTemplateSms(mobile, tempId, msgContentParams);
                        String status = ValueUtil.getStrFromXml(result,"status");
                        if(!ImConstant.SMS_PUSH_OUT_SUCCESS.equals(status)){
                            throw new ValidationException(result);
                        }
                    } catch (Exception e) {
                        deleteRecord(redisKey);
                        logger.error("发送模板短信失败 mobile={},tempId={}",mobile,tempId,e);
                    }
                }
            });
        } catch (Exception e) {
            logger.error("发送模板短信失败",e);
        }


    }

    /**
     * @desc 直接发送短信,但需要短信中心审核
     * @author wangxianchen
     * @create 2017-10-25
     * @param mobile
     * @param content
     * @param redisKey 可以为null
     */
    @Override
    public void sendSms(String mobile, String content,String redisKey){
        try {
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String result = SendSmsUtil.sendSms(mobile,content);
                        String status = ValueUtil.getStrFromXml(result,"status");
                        if(!ImConstant.SMS_PUSH_OUT_SUCCESS.equals(status)){
                            throw new ValidationException(result);
                        }
                    } catch (Exception e) {
                        deleteRecord(redisKey);
                        logger.error("短信发送失败 mobile={},content={}",mobile,content,e);
                    }
                }
            });
        } catch (Exception e) {
            logger.error("短信发送失败",e);
        }
    }

    private void deleteRecord(String redisKey){
        if(StringUtils.isNotBlank(redisKey)){
            redisService.remove(redisKey);
        }
    }
}
