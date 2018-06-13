package com.hhly.elasticsearch.annotations;

/**
 * @author wangxianchen
 * @create 2017-10-18
 * @desc 日期格式  详见 https://www.elastic.co/guide/en/elasticsearch/reference/1.4/mapping-date-format.html
 */
public enum DateFormat {

    yyyyMMddHHmmss("yyyy-MM-dd HH:mm:ss"),

    yyyyMMdd("yyyy-MM-dd");

    String label;

    DateFormat(String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}