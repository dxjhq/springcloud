package com.hhly.api.enums;

/**
 * 时间周期枚举(秒)
 * @author pegnchao
 * @create 2017-09-07
 * @desc
 */
public enum TimeEnum {
    FOREVER(-1,"永久"),
    HALF_YEAR(15552000, "半年"),
    THREE_MONTH(7776000, "三个月"),
    MONTH(2592000,"一个月"),
    HALF_MONTH(1296000,"半个月"),
    WEEK(604800,"一周"),
    THREE_DAY(259200,"三天"),
    DAY(86400,"一天"),
    TWO_HOUR(7200,"两小时"),
    HOUR(3600,"一小时"),
    HALF_HOUR(1800,"半小时");

    private int seconds;
    private String cycle;
    TimeEnum(int seconds,String cycle){
        this.seconds =seconds;
        this.cycle=cycle;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }
}
