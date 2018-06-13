package com.hhly.web.annotations;

import com.hhly.api.enums.VersionEnum;
import com.hhly.utils.web.RequestUtil;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;

public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

    private VersionEnum version;
    /** 参数 version 表示: 标注在 controller 方法上的注解 ApiVersion 里面的值 */
    public ApiVersionCondition(VersionEnum version) {
        this.version = version;
    }

    public ApiVersionCondition combine(ApiVersionCondition other) {
        return new ApiVersionCondition(other.version);
    }

    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
        // 从请求中获取到的版本. 多个纬度:「user-agent」或「header(param) 中的 app-ver」参数
        Integer reqVer = RequestUtil.getAppVersion();
        // 如果请求的是 v3, Controller 中标注的有 v v1 v2 和 v5 四个方法, 则 v1 和 v2 会返回, 而 v5 则不会, v 不会参与对比
        return (reqVer != null && version != null && reqVer >= version.getCode()) ? this : null;
    }

    /**
     * 从上面的匹配中将会导致匹配到多个, 如上面的 v3, 将会返回三个: v v1 v2.<br>
     * spring 会基于下面这个方法返回的值做排序, 然后将排序后的第一个方法做为最佳匹配, 如果多于一个则将第二个做为第二匹配.<br>
     * 而后将第一匹配和第二匹配再按照这个方法进行比较. 如果两个匹配的比较结果一致, 将会抛出两个方法对于这个请求太过暧昧的异常.<br>
     * 将最佳匹配做为请求的处理方法去执行!
     *
     * @see org.springframework.web.servlet.handler.AbstractHandlerMethodMapping#lookupHandlerMethod
     */
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        return (other != null && version != null) ? (other.version.getCode() - version.getCode()) : 0;
    }
}