package com.hhly.im.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.LinkedList;

/**
 * @author wangxianchen
 * @create 2017-10-25
 * @desc 消息内容
 */
@Setter
@Getter
@ToString
public class SmsContent {

    //消息模板ID
    private String tempId;
    //消息内容参数
    private LinkedList<String> msgContentParams;
    //文本
    private String text;
    //创建时间
    private Date createTime;
}
