package com.hhly.api.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.hhly.api.enums.ErrorCodeEnum;
import com.hhly.api.enums.IEnum;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
* @author wangxianchen
* @create 2017-09-26
* @desc 通用结果返回对象
*/
public class ResultObject implements Serializable{

    private static final long serialVersionUID = 3659991993289735551L;

    private int code;

    private String msg;

    private Object data;

    public ResultObject(){
        this.code = ErrorCodeEnum.SUCCESS.code();
        this.msg = ErrorCodeEnum.SUCCESS.message();
        this.data = null;
    }

    public ResultObject(IEnum errorCodeEnum){
        this.code = errorCodeEnum.code();
        this.msg = errorCodeEnum.message();
        this.data = null;
    }

    public ResultObject(IEnum errorCodeEnum,Object data){
        this.code = errorCodeEnum.code();
        this.msg = errorCodeEnum.message();
        this.data = data;
    }
    @JSONField(serialize=false)
    @JsonIgnore
    public void doSuccess(){
        this.success(ErrorCodeEnum.SUCCESS.code(),ErrorCodeEnum.SUCCESS.message(),null);
    }
    @JSONField(serialize=false)
    @JsonIgnore
    public void success(String msg){
        this.success(ErrorCodeEnum.SUCCESS.code(),msg,null);
    }
    @JSONField(serialize=false)
    @JsonIgnore
    public void success(String msg,Object data){
        this.success(ErrorCodeEnum.SUCCESS.code(),msg,data);
    }
    @JSONField(serialize=false)
    @JsonIgnore
    private void success(int code,String msg,Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    @JSONField(serialize=false)
    @JsonIgnore
    public void doFail(){
        this.code = ErrorCodeEnum.FAIL.code();
        this.msg = ErrorCodeEnum.FAIL.message();
        this.data = null;
    }
    @JSONField(serialize=false)
    @JsonIgnore
    public void fail(String msg){
        this.code = ErrorCodeEnum.FAIL.code();
        this.msg = msg;
        this.data = null;
    }
    @JSONField(serialize=false)
    @JsonIgnore
    public void fail(IEnum errorCodeEnum){
        this.code = errorCodeEnum.code();
        this.msg = errorCodeEnum.message();
        this.data = null;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    /**
     * 结果是否为成功
     * @return
     */
    @JSONField(serialize=false)
    @JsonIgnore
    public boolean isSuccess(){
        if(this.getCode() == ErrorCodeEnum.SUCCESS.code()){
            return true;
        }
        return false;
    }

    /**
     * 结果是否为失败
     * @return
     */
    @JSONField(serialize=false)
    @JsonIgnore
    public boolean isFail(){
        if(this.getCode() != ErrorCodeEnum.SUCCESS.code()){
            return true;
        }
        return false;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    /**
     * @desc 设置返回数据对象
     * @author wangxianchen
     * @create 2017-11-06
     * @param data
     */
    public void setData(Object data) {
        if(data != null && data instanceof PageList){
           Map<String,Object> map = new HashMap<>();
           map.put("list",data);
           map.put("paginator",((PageList) data).getPaginator());
           this.data = map;
        }else{
            this.data = data;
        }
    }

    /**
     * @desc 设置返回数据对象
     * @author wangxianchen
     * @create 2017-11-06
     * @param data 数据对象
     * @param paginator 分页信息
     */
    public void setData(Object data,Paginator paginator) {
            Map<String,Object> map = new HashMap<>();
            map.put("list",data);
            map.put("paginator",paginator);
            this.data = map;
    }

    /**
     * @desc 设置返回数据对象
     * @author wangxianchen
     * @create 2017-11-06
     * @param items 数据对象
     * @param total 记录总数
     */
    public void setData(Object items,long total) {
        Map<String,Object> map = new HashMap<>();
        map.put("items",items);
        map.put("total",total);
        this.data = map;
    }
}
