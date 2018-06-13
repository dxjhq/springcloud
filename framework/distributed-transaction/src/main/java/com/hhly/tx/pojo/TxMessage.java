package com.hhly.tx.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangxianchen
 * @create 2017-11-16
 * @desc 事务消息
 */
@Getter
@Setter
@ToString
public class TxMessage{
    //消息ID
    private String id;
    //调用方消息ID
    private String callerMsgId;
    //服务名称
    private String serviceName;
    //状态
    private Integer state;
    //创建时间
    private String createTime;
    //更新时间
    private String updateTime;
    //回滚ID
    private String rollbackId;
    //回滚ServiceBean
    private String rollbackBeanType;
    //回滚方法
    private String rollbackMethod;
    //异常编码
    private String errCode;
    //抛出异常的类型className
    private String errType;
}
