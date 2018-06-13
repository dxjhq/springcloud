package com.hhly.utils.web;

import com.hhly.api.dto.ResultObject;
import com.hhly.api.enums.VersionEnum;
import com.hhly.api.model.AppClient;
import com.hhly.utils.json.JsonUtil;
import com.hhly.utils.LogUtil;
import com.hhly.utils.ValueUtil;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * <span style="color:red;">!!!此工具类请只在 controller 中调用!!!</span>
 */
public final class RequestUtil {

    public static final String USER_AGENT = "User-Agent";
    public static final String REFERRER = "referer";
    public static final String APP_VER = "app-ver";

    /**
     * 获取真实客户端IP
     * 关于 X-Forwarded-For 参考: http://zh.wikipedia.org/wiki/X-Forwarded-For<br>
     * 这一 HTTP 头一般格式如下:
     * X-Forwarded-For: client1, proxy1, proxy2,<br><br>
     * 其中的值通过一个 逗号 + 空格 把多个 IP 地址区分开, 最左边(client1)是最原始客户端的IP地址,
     * 代理服务器每成功收到一个请求，就把请求来源IP地址添加到右边
     */
    public static String getRealIp() {
       return getRealIp(getRequest());
    }
    public static String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ValueUtil.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }

        ip = request.getHeader("X-Forwarded-For");
        if (ValueUtil.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为 真实 ip
            return ip.split(",")[0].trim();
        }

        ip = request.getHeader("Proxy-Client-IP");
        if (ValueUtil.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (ValueUtil.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }

        ip = request.getHeader("HTTP_CLIENT_IP");
        if (ValueUtil.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }

        ip = request.getHeader("X-Cluster-Client-IP");
        if (ValueUtil.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }
        return request.getRemoteAddr();
    }
    /**
     * 判断当前请求是否是 ajax 请求, 是 ajax 则返回 true
     */
    public static boolean isAjaxRequest() {
        HttpServletRequest request = getRequest();
        String requestedWith = request.getHeader("X-Requested-With");
        String contentType = request.getHeader("Content-Type");
        return (requestedWith != null && "XMLHttpRequest".equals(requestedWith))
                || (contentType != null && "application/json".startsWith(contentType))
                || ValueUtil.isNotBlank(getHeaderOrParam("_ajax"))
                || ValueUtil.isNotBlank(getHeaderOrParam("_json"));
    }

    /**
     * <pre>
     * iOS:
     *   user:   LYLawyerPlatform/1.0.1 (iPhone; iOS 9.1; Scale/2.00)
     *   lawyer: LYLawyerPlatform_Lawyer/1.0.2 (iPhone; iOS 10.2; Scale/3.00)
     *
     * Android:
     *   user:   LYLawyerPlatform/1.0.2 Mozilla/5.0 (Linux; Android 5.1.1; NX511J Build/LMY47V; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/46.0.2490.76 Mobile Safari/537.36
     *   lawyer: LYLawyerPlatform_Lawyer/1.0.1 Mozilla/5.0 (Linux; Android 5.1.1; SM-G5500 Build/LMY47X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/49.0.2623.105 Mobile Safari/537.36
     * </pre>
     */
    public static String userAgent() {
        return getRequest().getHeader(USER_AGENT);
    }

    /**
     * app 请求时的版本. <span style="color:red;">!!!注意, 返回的值可能是空的!!!</span>
     */
    public static VersionEnum getVersion() {
        return getAppClient().getVersion();
    }

    /**
     * app 请求时的版本数字. <span style="color:red;">!!!注意, 返回的值可能是空!!!</span>
     */
    public static Integer getAppVersion() {
        return getAppClient().getAppVersion();
    }

    /**
     * app 请求时的来源: 用户端, 律师端
     */
    public static String getUserType() {
        return getAppClient().getAppUserType();
    }

    private static AppClient getAppClient() {
        return AppClient.get(userAgent(), getHeaderOrParam(APP_VER));
    }

    /**
     * 请求来源(1.pc, 2.iOS, 3.安卓, 4.其他)
     */
    public static Integer getOsType() {
        return getAppClient().getAppOsType();
    }

    /**
     * 判断当前请求是否来自移动端, 来自移动端则返回 true
     */
    public static boolean isMobileRequest() {
        return ValueUtil.checkMobile(userAgent());
    }

    /**
     * 格式化参数, 如果是文件流(form 表单中有 type="multipart/form-data" 这种), 则不打印出参数
     *
     * @return 示例: id=xxx&name=yyy
     */
    public static String formatParam() {
        // return getRequest().getQueryString(); // 没有时将会返回 null

        HttpServletRequest request = getRequest();
        return ServletFileUpload.isMultipartContent(request)
                ? "(content-type start with multipart/) uploading file"
                : ValueUtil.formatParam(request.getParameterMap());
    }

    /**
     * 返回 url 并且拼上参数, 非 get 请求将忽略参数
     */
    public static String getUrl() {
        String url = getRequestUrl();
        if (!isAjaxRequest() && "GET".equals(getRequest().getMethod().toUpperCase())) {
            String param = formatParam();
            if (ValueUtil.isNotBlank(param)) {
                url += ("?" + param);
            }
        }
        return url;
    }

    /**
     * <pre>
     * 获取完整的 url 地址(此方法与 request.getRequestURL 等价).
     *
     * <a href="http://tomcat.apache.org/tomcat-8.0-doc/api/org/apache/catalina/valves/RemoteIpValve.html">https</a>
     * 前台走的是 https, 运维经过 nginx 后再转到 tomcat 却成了 http 时:
     *
     * 先在 nginx 的 location 中加: proxy_set_header X-Forwarded-Proto $scheme;
     *
     * 再在 tomcat 的 server.xml 中 context 节点添加下面的配置, 代码就不需要任何变动
     * &lt;Valve className="org.apache.catalina.valves.RemoteIpValve" remoteIpHeader="X-Forwarded-For"
     *  protocolHeader="X-Forwarded-Proto" protocolHeaderHttpsValue="https" /&gt;
     * </pre>
     *
     * @see org.apache.catalina.connector.Request#getRequestURL()
     */
    public static String getRequestUrl() {
        HttpServletRequest request = getRequest();
        // return request.getRequestURL().toString();

        String scheme = request.getScheme();
        int port = request.getServerPort();
        if (port < 0) {
            port = 80;
        }

        StringBuilder sbd = new StringBuilder();
        sbd.append(scheme).append("://").append(request.getServerName());
        if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443))) {
            sbd.append(':').append(port);
        }
        sbd.append(request.getRequestURI());
        return sbd.toString();
    }

    /**
     * 获取上一个请求的 url. 先从 requestUrl 读, 而再从 referrer 读.
     * 如果有值且值是以指定域名开头的, 且不能是主域名, 也不是要放过的 url, 就将此 url 转义了返回
     * @param domain    主域名
     * @param letGoUrls 放过的 url 数组
     */
    public static String getLastUrl(String domain, String[] letGoUrls) {
        String last = getUrl();
        if (checkUrl(last, domain, letGoUrls)) {
            return ValueUtil.urlEncode(last);
        }

        last = getReferrer();
        if (checkUrl(last, domain, letGoUrls)) {
            return ValueUtil.urlEncode(last);
        }
        return ValueUtil.EMPTY;
    }

    /**
     * 获取request里的所有header
     * @param request
     * @return
     */
    public static HttpHeaders getHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        String method = request.getMethod();
        if (method.equalsIgnoreCase(HttpMethod.POST.toString())) { //所有的post 请求都是json
            headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        }
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        Enumeration<String> enmus = request.getHeaderNames();
        while (enmus.hasMoreElements()) {
            String name = enmus.nextElement();
            headers.add(name, request.getHeader(name));
        }
        return headers;
    }

    /**
     * 获取request里的所有header
     * @return
     */
    public static HttpHeaders getHeaders() {
        return getHeaders(getRequest());
    }

    /**
     * 获取请求实体
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static HttpEntity<String> getHttpEntity(HttpServletRequest request) throws Exception {
        return new HttpEntity<String>(HttpHelper.getRequestPostStr(request), getHeaders(request));
    }

    /**
     * 获取请求实体
     * @return
     * @throws Exception
     */
    public static HttpEntity<String> getHttpEntity() throws Exception {
        return getHttpEntity(getRequest());
    }

    /**
     * 获取Get请求参数集合
     * @param request
     * @return
     */
    public static Map<String, String[]> getParameterMap(HttpServletRequest request) {
        return request.getParameterMap();
    }

    /**
     * 获取Get请求参数集合
     * @return
     */
    public static Map<String, String[]> getParameterMap() throws Exception {
        return getParameterMap(getRequest());
    }

    /**
     * 返回的 url 不能为空, 跟主域名不一样, 且不包含在指定的 url 里面就返回 true
     */
    private static boolean checkUrl(String backUrl, String domain, String[] letGoUrls) {
        if (ValueUtil.isNotBlank(backUrl)) {
            domain = ValueUtil.addSuffix(domain);
            // 不能完全跟域名一样, 也不能在指定的 url 里面
            return !backUrl.equals(domain) && !letGo(backUrl, domain, letGoUrls);
        }
        return false;
    }

    /**
     * 传入的 url 是在指定的里面就返回 true
     */
    private static boolean letGo(String backUrl, String domain, String[] letGoUrls) {
        domain = ValueUtil.addSuffix(domain);
        for (String url : letGoUrls) {
            // url 里面有以 / 开头就去掉, domain 里面已经带了
            if (ValueUtil.isNotBlank(url) && url.startsWith("/")) url = url.substring(1);
            if (backUrl.startsWith(domain + url)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 请求头里的 referer 这个单词拼写是错误的, 应该是 referrer, 然而历史遗留原因导致这个问题永远不会被更正
     */
    public static String getReferrer() {
        return getRequest().getHeader(REFERRER);
    }

    /**
     * 先从请求头中查, 为空再从参数中查
     */
    public static String getHeaderOrParam(String param) {
        HttpServletRequest request = getRequest();
        String header = request.getHeader(param);
        if (ValueUtil.isBlank(header)) header = request.getParameter(param);
        return ValueUtil.isBlank(header) ? ValueUtil.EMPTY : header.trim();
    }

    /**
     * 格式化头里的参数: 每对值以换行隔开, 键值以冒号分隔
     */
    public static String formatHeadParam() {
        HttpServletRequest request = getRequest();

        StringBuilder sbd = new StringBuilder();
        int i = 0;
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            if (i > 0) sbd.append("\n");
            String headName = headerNames.nextElement();
            sbd.append(headName).append(" : ").append(request.getHeader(headName));
            i++;
        }
        return sbd.toString();
    }

    /**
     * 将「json 结果」以 json 格式输出
     */
    public static void toJson(ResultObject result, HttpServletResponse response) throws IOException {
        toJson(JsonUtil.toRender(result), response);
    }

    /**
     * 将「字符串」以 json 格式输出
     */
    public static void toJson(String result, HttpServletResponse response) throws IOException {
        render("application/json", result, response);
    }

    private static void render(String type, String result, HttpServletResponse response) throws IOException {
        if (LogUtil.ROOT_LOG.isInfoEnabled())
            LogUtil.ROOT_LOG.info("return json: " + result);

        response.setContentType(type + ";charset=UTF-8;");
        response.getWriter().write(result);
    }

    public static HttpHeaders createHeader() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        List<MediaType> acceptList = new ArrayList<MediaType>();
        acceptList.add(MediaType.APPLICATION_JSON);
        acceptList.add(MediaType.ALL);
        requestHeaders.setAccept(acceptList);

        requestHeaders.setConnection("Keep-Alive");
        return requestHeaders;
    }

    /**
     * 将「字符」以 html 格式输出. 不常见! 这种只会在一些特殊的场景用到
     */
    public static void toHtml(String result, HttpServletResponse response) throws IOException {
        render("text/html", result, response);
    }

    private static ServletRequestAttributes getRequestAttributes() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
    }

    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }
}
