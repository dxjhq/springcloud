package com.hhly.token.model;

import java.io.Serializable;

/**
 * @author pengchao
 * @create 2017-12-06
 * @desc 用户信息
 */
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String KEY_ID = "x-uid";
    public static final String KEY_USERNAME = "x-username";
    public static final String KEY_NICKNAME = "x-nickname";
    public static final String KEY_CELLPHONE = "x-cellphone";
    public static final String KEY_USERTYPE = "x-usertype";

    /**
     * 用户ID
     */
    private String id;

    /**
     * 账号
     */
    private String username;

    /**
     * 账号类型
     */
    private String usertype;

    /**
     * 昵称（中文名称）
     */
    private String nickname;

    /**
     * 手机号
     */
    private String cellphone;

    /**
     * 保证唯一性的Key
     */
    private String sessionkey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getSessionkey() {
        return sessionkey;
    }

    public void setSessionkey(String sessionkey) {
        this.sessionkey = sessionkey;
    }
}