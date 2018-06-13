package com.hhly.api.model;

/**
 * Created by hhly-pc on 2017/8/17.
 */
@Deprecated
public final  class UrlMethod {
    public  static final String GET="GET";
    public static final String POST="POST";
    public static final String DELETE="DELETE";
    public static final String PUT="PUT";

    private String url;

    private String method;

    public UrlMethod(){}

    public UrlMethod(String url){
        this.url=url;
        this.method= UrlMethod.GET;
    }

    public UrlMethod(String url, String method){
        this.url=url;
        this.method=method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
