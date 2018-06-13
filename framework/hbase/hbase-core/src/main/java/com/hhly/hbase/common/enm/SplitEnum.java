package com.hhly.hbase.common.enm;

/**
 * 字符串分隔符枚举类型枚举
 */
public enum SplitEnum {
    COMMA(",","英文格式逗号分隔符"),

    COLON(":","英文格式冒号分隔符"),

    SEMICOLON(";","英文格式分号分隔符");

    private String code;

    private String message;

    SplitEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
