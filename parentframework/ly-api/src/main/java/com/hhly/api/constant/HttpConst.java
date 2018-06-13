package com.hhly.api.constant;

/**
 * @author pengchao
 * @create 2017-12-07
 * @desc Http公共常量池
 */
public class HttpConst {
    /**
     * 报文头
     */
    public final static String REQUEST_HEADER_KEY_APP = "app";
    public final static String REQUEST_HEADER_KEY_IMEI = "imei";
    public final static String REQUEST_HEADER_KEY_TOKEN = "token";
    public final static String REQUEST_HEADER_KEY_SID = "sid";
    public final static String REQUEST_HEADER_KEY_OS = "platform";
    public final static String REQUEST_HEADER_KEY_SIGN = "sign";
    public final static String REQUEST_HEADER_USER_ID = "uid";
    public final static String REQUEST_HEADER_API_VERSION = "ver";

    /**
     * <pre>
     * 在 servlet 规范中有 forward 和 redirect 两种页面跳转.
     *   forward 不会改变页面的请求地址, 而且前一个请求的 request 和 response 在下一个请求中还有效.
     *   redirect 正好不同, 要传值得使用 参数拼接 或者放在 session 里(都有利弊)
     *
     * 在 spring mvc 的 controller 上返回 String 时
     *   return "forward:/some/one" => 转发到 /some/one 的 controller 方法上去. mvc 内部的异常处理就是基于这种方式
     *   return "some/one"          => 转发到 classpath:some/one.jsp 页面去(如果是 jsp 的话)
     *   return "/some/one"         => 同 some/one
     * </pre>
     */
    public static final String REQUEST_FORWARD_PREFIX = "forward:";

    /**
     * <pre>
     * 在 servlet 规范中有 forward 和 redirect 两种页面跳转.
     *   forward 不会改变页面的请求地址, 而且前一个请求的 request 和 response 在下一个请求中还有效.
     *   redirect 正好不同, 要传值得使用 参数拼接 或者放在 session 里(都有利弊)
     *
     * 要传递参数, 可以使用 RedirectAttributes 或者直接拼在 url 上
     * </pre>
     * @see org.springframework.web.servlet.mvc.support.RedirectAttributes
     */
    public static final String REQUEST_REDIRECT_PREFIX = "redirect:";
}
