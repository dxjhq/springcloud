package com.hhly.utils.web;

import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.hhly.utils.ValueUtil;
import com.hhly.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpUtil {

    private static final int TIME_OUT = 3 * 1000;
    private static final String GET = "GET";
    private static final String POST = "POST";

    public static String get(String url) {
        return get(url, null);
    }

    public static String get(String url, Map<String, Object> params) {
        return get(url, params, TIME_OUT);
    }

    public static String get(String url, int timeout) {
        return get(url, null, timeout);
    }

    public static String get(String url, Map<String, Object> params, int timeout) {
        // get 请求是把参数跟在后面
        if (params != null && params.size() > 0) {
            url += (ValueUtil.checkRegexWithRelax(url, "\\?") ? "&" : "?") + ValueUtil.formatParam(params);
        }
        return connection(url, GET, ValueUtil.EMPTY, timeout);
    }

    public static String post(String url, Map<String, Object> params) {
        return post(url, params, TIME_OUT);
    }

    public static String post(String url, Map<String, Object> params, int timeout) {
        return connection(url, POST, ValueUtil.formatParam(params), timeout);
    }

    public static String post(String url, String params) {
        return connection(url, POST, params, TIME_OUT);
    }

    /** 下载 url 到指定的文件 */
    public static void download(String url, String file) throws IOException {
        InputStream inputStream = response((HttpURLConnection) new URL(url).openConnection());
        if (inputStream != null) {
            ByteStreams.copy(inputStream, new FileOutputStream(file));
        }
    }

    /** 删除指定的文件 */
    public static void delete(String file) throws IOException {
        if (!new File(file).delete()) {
            if (LogUtil.ROOT_LOG.isErrorEnabled()) LogUtil.ROOT_LOG.error("无法删除({})", file);
        }
    }

    private static InputStream response(HttpURLConnection conn) {
        try {
            return conn.getInputStream();
        } catch (IOException e) {
            if (LogUtil.ROOT_LOG.isWarnEnabled()) {
                LogUtil.ROOT_LOG.warn(String.format("request (%s) exception", conn.getURL()), e);
            }
            return conn.getErrorStream();
        }
    }

    private static String connection(String url, String method, String params, int timeout) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        if (LogUtil.ROOT_LOG.isInfoEnabled()) {
            // 只有方法是 get 时才输出参数, 否则在日志中忽略
            String param = GET.equalsIgnoreCase(method) && ValueUtil.isNotBlank(params) ? (", " + params) : ValueUtil.EMPTY;
            LogUtil.ROOT_LOG.info("request ({}, {}{})", method, url, param);
        }

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(timeout);
            connection.setConnectTimeout(timeout);
            connection.setRequestMethod(method);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            if (ValueUtil.isNotBlank(params)) {
                Writer writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
                writer.write(params);
                writer.flush();
                writer.close();
            }

            InputStream inputStream = response(connection);
            if (inputStream != null) {
                return CharStreams.toString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            if (LogUtil.ROOT_LOG.isWarnEnabled()) {
                String param = GET.equalsIgnoreCase(method) ? (", " + params) : ValueUtil.EMPTY;
                LogUtil.ROOT_LOG.warn(String.format("request (%s, %s%s) exception", method, url, param), e);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return ValueUtil.EMPTY;
    }

    /**
     * 获取访问者IP
     *
     * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
     *
     * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
     * 如果还不存在则调用Request .getRemoteAddr()。
     *
     * @param request
     * @return
     */
    private static String getIpAddr(HttpServletRequest request){
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }
}