package com.hhly.tx.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wangxianchen
 * @create 2017-12-07
 * @desc 服务端响应信息
 */
@Getter
@Setter
@ToString
public class TxResErroInf {

    //回滚ID
    private String rollbackId;

    //异常编码
    private String errCode;

    //抛出异常的类型 className
    private String errType;
}
