package com.hhly.im.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * @author wangxianchen
 * @create 2017-10-23
 * @desc
 */
public interface BaseEmailService {

    void sendSimpleMail(final SimpleMailMessage message)  throws Exception;

    void sendHtmlMail(final MimeMessageHelper messageHelper);

}
