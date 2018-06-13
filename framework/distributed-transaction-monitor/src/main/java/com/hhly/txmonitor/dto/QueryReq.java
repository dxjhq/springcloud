package com.hhly.txmonitor.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wangxianchen
 * @create 2017-11-30
 * @desc
 */
@Getter
@Setter
@ToString
public class QueryReq {

    private String serviceName;

    private String msgKey;

    private long delay; //延迟时间

    private long period; //执行间隔
}
