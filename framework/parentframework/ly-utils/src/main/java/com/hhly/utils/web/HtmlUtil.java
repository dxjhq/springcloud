package com.hhly.utils.web;

import com.hhly.utils.ValueUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cy on 2017/2/28.
 * 处理html的相关类
 */
public final class HtmlUtil {

    private final static int INDEX = 30;

    private static final Pattern PAGE_REGEX = Pattern.compile("(?s)<!--.*?-->");
    private static final Pattern CSS_REGEX = Pattern.compile("(?is)(<style.*?>)(.*?)(</style.*?>)");
    private static final Pattern JS_REGEX = Pattern.compile("(?is)(<script.*?>)(.*?)(</script.*?>)");
    private static final Pattern LABEL_REGEX = Pattern.compile("(?s)<.*?>");
    private static final Pattern SPACE_REGEX = Pattern.compile("&nbsp;");
    private static final Pattern MULTI_SPACE_REGEX = Pattern.compile("\\s{2,}");

    private static final Pattern IMG_REGEX = Pattern.compile("(?is)<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");

    /** 去掉 html 源码中的注释和 script 和 style, 再去掉所有标签并截取 30 位的字符返回 */
    public static String matcherText(String content) {
        return matcherText(content, INDEX);
    }

    /** 去掉 html 源码中的注释和 script 和 style, 再去掉所有标签并截取指定长度的字符(小于 0 则默认是 30)返回 */
    public static String matcherText(String content, int index) {
        if (ValueUtil.isBlank(content)) return ValueUtil.EMPTY;

        content = replaceRegex(PAGE_REGEX, content, ValueUtil.EMPTY);
        content = replaceRegex(JS_REGEX, content, ValueUtil.EMPTY);
        content = replaceRegex(CSS_REGEX, content, ValueUtil.EMPTY);
        content = replaceRegex(LABEL_REGEX, content, ValueUtil.EMPTY);
        content = replaceRegex(SPACE_REGEX, content, ValueUtil.SPACE);
        content = replaceRegex(MULTI_SPACE_REGEX, content, ValueUtil.SPACE);
        content = content.trim();

        if (index < 0) index = INDEX;
        if (index > content.length()) index = content.length();
        return content.substring(0, index);
    }

    private static String replaceRegex(Pattern pattern, String content, String place) {
        return ValueUtil.isBlank(content) ? ValueUtil.EMPTY : pattern.matcher(content).replaceAll(place);
    }

    /** 获取 html 源码中的第一个 img 标签, 并将其 src 返回 */
    public static String matchFirstImg(String content) {
        if (ValueUtil.isBlank(content)) return ValueUtil.EMPTY;

        Matcher imageMatcher = IMG_REGEX.matcher(content);
        return imageMatcher.find() ? imageMatcher.group(1) : ValueUtil.EMPTY;
    }
}
