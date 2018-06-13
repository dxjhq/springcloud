package com.hhly.im.service;

import freemarker.template.Template;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

/**
 * @author wangxianchen
 * @create 2017-10-24
 * @desc 邮件服务默认实现类
 */
public abstract class AbstractEmailService implements BaseEmailService {

    private Logger logger = LoggerFactory.getLogger(AbstractEmailService.class);
    
    @Resource(name = "ImThreadPoolTaskExecutor")
    protected ThreadPoolTaskExecutor taskExecutor;

    @Resource
    protected JavaMailSender javaMailSender;

    @Resource
    protected FreeMarkerConfig freeMarkerConfig;

    @Value("${spring.mail.username}")
    protected String sender;

    public SimpleMailMessage createSimpleMailMessage(){
        return new SimpleMailMessage();
    }
    public MimeMessageHelper createMimeMessageHelper() throws MessagingException{
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        return new MimeMessageHelper(mimeMessage, true);
    }

    /**
     * @desc 使用多线程发送简单邮件
     * @author wangxianchen
     * @create 2017-10-24
     * @param message
     * @throws Exception
     */
    @Override
    public void sendSimpleMail(final SimpleMailMessage message) throws Exception{
        Assert.isTrue(StringUtils.isNotEmpty(message.getFrom()),"From address must not be null");
        Assert.isTrue(ArrayUtils.isNotEmpty(message.getTo()),"To address must not be null");
        try {
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    javaMailSender.send(message);
                }
            });
        } catch (Exception e) {
            logger.error("文本邮件发送失败",e);
        }

    }


    /**
     * @desc 使用多线程发送HTML邮件
     * @author wangxianchen
     * @create 2017-10-24
     * @param messageHelper
     */
    @Override
    public void sendHtmlMail(final MimeMessageHelper messageHelper){
        try {
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    javaMailSender.send(messageHelper.getMimeMessage());
                }
            });
        } catch (Exception e) {
            logger.error("HTML邮件发送失败",e);
        }
    }

    /**
     * @desc 根据模板ID,获取模板内容
     * @author wangxianchen
     * @create 2017-10-24
     * @param contentMap
     * @param templateId
     * @return
     * @throws Exception
     */
    public String getTemplateStr(Map<String,Object> contentMap, String templateId) throws Exception {
        Template t = freeMarkerConfig.getConfiguration().getTemplate(templateId);
        return FreeMarkerTemplateUtils.processTemplateIntoString(t, contentMap);
    }

    /**
     * @desc 获取发送人
     * @author wangxianchen
     * @create 2017-10-24
     * @return
     */
    public String getSender() {
        return sender;
    }
}
