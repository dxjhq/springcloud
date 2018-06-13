package com.hhly.utils.converter;

import com.hhly.utils.ValueUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.util.HtmlUtils;

import java.nio.charset.StandardCharsets;

/** 转义 前台传递过来的参数, 过滤 XSS(Cross site script 跨站脚本)攻击 */
public class StringTrimAndEscapeConverter implements Converter<String, String> {

    @Override
    public String convert(String source) {
        if (source == null) return ValueUtil.EMPTY;



        // 如果是密码字段, 不应该 trim
        return HtmlUtils.htmlEscape(source/*.trim()*/, StandardCharsets.UTF_8.name());
    }
}
