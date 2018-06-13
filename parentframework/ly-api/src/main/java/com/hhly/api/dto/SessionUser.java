package com.hhly.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
/**
* @author wangxianchen
* @create 2017-08-29
* @desc session用户信息
*/
@Getter
@Setter
@NoArgsConstructor
public class SessionUser{

    //帐户ID
    private String userId;
    //帐户名称
    private String userName;
    //帐户手机号码
    private String phoneNo;
    //来自登录应用
    private String loginAppCode;
    //登录日期
    private String loginDate;
    //登录IP
    private String loginIP;
    //登录设备类型标识
    private String loginDeviceType;
    //登录设备ID
    private String loginDeviceId;
    //终端设备(浏览器)版本标识
    private String userAgent;
    //该对象保存在redis里的key
    private String key;
}