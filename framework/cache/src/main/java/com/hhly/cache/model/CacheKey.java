package com.hhly.cache.model;

/**
 * @author pengchao
 * @create 2017-12-27
 * @desc 缓存Key实体
 */
public class CacheKey {
    //所属服务名(编码)
    private String serverName;
    //所属功能模块名(编码)
    private String moduleName;
    //业务Id
    private String bussinessId;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getBussinessId() {
        return bussinessId;
    }

    public void setBussinessId(String bussinessId) {
        this.bussinessId = bussinessId;
    }
}
