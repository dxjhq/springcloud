package com.hhly.im.service;
import com.hhly.api.exception.ValidationException;
import com.hhly.im.constant.ImConstant;
import com.hhly.im.exception.NotExceedingIntervalTime;
import com.hhly.im.pojo.SmsContent;
import com.hhly.utils.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author wangxianchen
 * @create 2017-10-24
 * @desc 手机短信服务默认实现
 */
@Service
public class SmsService extends AbstractSmsService {

    /**
     * @desc 消息发送,根据业务类型配置的参数来判断其两次发送的间隔时间,并保存消息内容
     * @author wangxianchen
     * @create 2017-10-25
     * @param bizType
     * @param mobile
     * @param tempId
     * @param msgContentParams
     * @throws Exception
     */
    public void sendTemplateSms(String bizType, String mobile, String tempId, LinkedList<String> msgContentParams) throws Exception {
        Assert.isTrue(StringUtils.isNotEmpty(bizType), "业务类型不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(mobile), "手机号码不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(tempId), "短信模板ID不能为空");
        Assert.isTrue(msgContentParams != null, "参数不能为空");

        Map<String, Integer> bizMap = smsConfigProperties.getBizMap();
        if (bizMap == null || bizMap.size() == 0 || !bizMap.containsKey(bizType)) {
            throw new ValidationException("业务类型未设置");
        }
        //间隔时间
        Integer interval = bizMap.get(bizType);
        Date now = new Date();
        String redisKey = ImConstant.SMS_SEND_RECORD_PREFIX + applicationName + ":" + mobile + ":" + bizType;
        if (interval != null && interval > 0) {
            Object obj = redisService.get(redisKey);
            if (obj != null) {
                SmsContent smsContent = (SmsContent)obj;
                //Date lastSendTime = DateUtil.converateDate(obj.toString());
                long seconds = DateUtil.getMillisBetweenSecod(smsContent.getCreateTime(), now);
                if (seconds < interval) { //两次获取验证码时间间隔小于默认间隔期
                    throw new NotExceedingIntervalTime("两次发送未超过间隔时间,请在"+(interval-seconds)+"秒后重试!");
                }
            }
        }

        SmsContent smsContent = new SmsContent();
        smsContent.setTempId(tempId);
        smsContent.setMsgContentParams(msgContentParams);
        smsContent.setCreateTime(now);
        redisService.set(redisKey, smsContent, ImConstant.SMS_SEND_RECORD_EXPIRE_TIME);
        sendTemplateSms(mobile, tempId, msgContentParams,redisKey);


    }

    /**
     * @desc 根据模板发送短信
     * @author wangxianchen
     * @create 2017-10-25
     * @param mobile
     * @param tempId
     * @param msgContentParams
     */
    public void sendTemplateSms(String mobile, String tempId, LinkedList<String> msgContentParams) throws Exception{
        Assert.isTrue(StringUtils.isNotEmpty(mobile), "手机号码不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(tempId), "短信模板ID不能为空");
        Assert.isTrue(msgContentParams != null, "参数不能为空");
        sendTemplateSms(mobile, tempId, msgContentParams,null);


    }

    /**
     * @desc 直接发送短信,这个需要短信中心人工审核
     * @author wangxianchen
     * @create 2017-10-25
     * @param mobile
     * @param content
     */
    public void sendSms(String mobile, String content) throws Exception{
        Assert.isTrue(StringUtils.isNotEmpty(mobile), "手机号码不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(content), "短信内容不能为空");
        sendSms(mobile,content,null);
    }
}