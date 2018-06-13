package com.hhly.api.enums;

/**
 * 这是公共的错误码,从0 ~ 100
 */
public enum ErrorCodeEnum implements IEnum {

    SUCCESS(1,"成功"),
    FAIL(0,"失败"),
    /***********  以上两项不可更改  **************/


    /*************公共 数据操作相关******************/
    SAVE_DATA_ERROR(2,"保存数据失败"),
    UPDATE_DATA_ERROR(3,"更新数据失败"),
    QUERY_DATA_ERROR(4,"查询数据失败"),
    DELETE_DATA_ERROR(5,"删除数据失败"),
    UPDATE_RECORD_NOT_EXIST_ERROR(6,"被更新的数据不存在"),
    INPUT_DATE_HAS_EXIST_ERROR(7,"输入数据已经存在"),
    REFRESH_DATA_ERROR(8,"%s，数据刷新失败"),
    PARAM_EXCEPTION(9, "参数异常: %s"),

    /***********公共 数据校验相关***************/
    PARSE_DATA_ERROR(10,"解析数据错误"),
    VERIFY_DATA_FAIL(11,"数据校验不通过"),
    PARSE_STR_TO_DATE_ERROR(12,"字符串转换日期出错"),
    PARSE_DATE_TO_STR_ERROR(13,"日期转换字符串出错"),
    DATA_EXPIRED(14,"数据已过期,请刷新操作"),

    NO_LOGIN(15,"未登录或会话已过期"),
    SESSION_EXPIRED(16,"会话已过期请重新登录"),

    SERVER_NO_FOUND(404,"没有发现服务"),

    UNKOWN_EXCEPTION(100, "系统压力山大,请稍后重试！");

    private int code;
    private String message;
    private String format;

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
        this.format=message;
    }

    public ErrorCodeEnum format(Object... msgArgs) {
        this.message = String.format(this.format, msgArgs);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return Integer.toString(code());
    }

    @Override
    public int code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }
}