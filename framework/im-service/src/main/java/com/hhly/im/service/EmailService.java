package com.hhly.im.service;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * @author wangxianchen
 * @create 2017-10-24
 * @desc email默认实现service
 */
@Service
public class EmailService extends AbstractEmailService {

    /**
     * @desc 发送文本邮件
     * @author wangxianchen
     * @create 2017-10-24
     * @param to 接收人
     * @param cc 抄送人
     * @param subject 主题
     * @param text 文本内容
     * @throws Exception
     */
    public void sendText(String[] to,String[] cc,String subject, String text) throws Exception{
        SimpleMailMessage simple = super.createSimpleMailMessage();
        simple.setFrom(sender);
        simple.setTo(to);
        if(ArrayUtils.isNotEmpty(cc)){
            simple.setCc(cc);
        }
        simple.setSubject(subject);
        simple.setText(text);
        super.sendSimpleMail(simple);
    }

    /**
     * @desc 发送文本邮件
     * @author wangxianchen
     * @create 2017-10-24
     * @param to 接收人
     * @param subject 主题
     * @param text 文本内容
     * @throws Exception
     */
    public void sendText(String[] to,String subject, String text) throws Exception{
        this.sendText(to,null,subject,text);
    }

    /**
     * @desc 发送HTML格式邮件
     * @author wangxianchen
     * @create 2017-10-24
     * @param to 接收人
     * @param cc 抄送人
     * @param subject 主题
     * @param html HTML格式字符串
     * @throws Exception
     */
    public void sendHtml(String[] to,String[] cc,String subject, String html) throws Exception {
        MimeMessageHelper mimeMessageHelper = super.createMimeMessageHelper();
        mimeMessageHelper.setFrom(sender);
        mimeMessageHelper.setTo(to);
        if(ArrayUtils.isNotEmpty(cc)){
            mimeMessageHelper.setCc(cc);
        }
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(html,true);
        super.sendHtmlMail(mimeMessageHelper);
    }

    /**
     * @desc 发送HTML格式邮件
     * @author wangxianchen
     * @create 2017-10-24
     * @param to 接收人
     * @param subject 主题
     * @param html HTML格式字符串
     * @throws Exception
     */
    public void sendHtml(String[] to,String subject, String html) throws Exception {
        this.sendHtml(to,null,subject,html);
    }

    /**
     * @desc 发送HTML格式邮件,内容是由freeMarker模板生成
     * @author wangxianchen
     * @create 2017-10-24
     * @param to 接收人
     * @param cc 抄送人
     * @param subject 主题
     * @param contentMap 内容
     * @param templateId 模板ID
     * @throws Exception
     */
    public void sendHtmlByTemplate(String[] to, String[] cc, String subject, Map<String,Object> contentMap, String templateId) throws Exception {
        MimeMessageHelper mimeMessageHelper = super.createMimeMessageHelper();
        mimeMessageHelper.setFrom(sender);
        mimeMessageHelper.setTo(to);
        if(ArrayUtils.isNotEmpty(cc)){
            mimeMessageHelper.setCc(cc);
        }
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(super.getTemplateStr(contentMap,templateId),true);
        super.sendHtmlMail(mimeMessageHelper);
    }

    /**
     * @desc 发送HTML格式邮件,内容是由freeMarker模板生成
     * @author wangxianchen
     * @create 2017-10-24
     * @param to 接收人
     * @param subject 主题
     * @param contentMap 内容
     * @param templateId 模板ID
     * @throws Exception
     */
    public void sendHtmlByTemplate(String[] to, String subject, Map<String,Object> contentMap, String templateId) throws Exception {
        this.sendHtmlByTemplate(to,null,subject,contentMap,templateId);
    }

}
