package com.hhly.hbase.common.utils;

import com.hhly.hbase.common.enm.SplitEnum;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @desc字符串处理工具类--某些方法具有定制型，
 *               仅限于统计功能中开发使用。
 *
 * @author BSW
 * @create 2017-09-06
 */
public class StringUtil {

    /**
     * @desc 处理逗号分隔的字符串为字符集合
     * @author BSW
     * @create 2017-09-30
     * @param sourceStr
     * @return
     */
    public static List<String> splitStringToList(String sourceStr){
        List<String> result = new ArrayList<>();
        if(isNotEmpty(sourceStr)){
            String[] strArray = sourceStr.split(SplitEnum.COMMA.getCode());
            result = Arrays.asList(strArray);
        }
        return result;
    }

    /**
     * @desc 字符串判断为空
     * @author BSW
     * @create 2017-09-21
     * @param sourceStr
     * @return
     */
    public static boolean isEmpty(String sourceStr){
        boolean result = false;
        if(sourceStr == null || "".equals(sourceStr)){
            result = true;
        }
        return result;
    }

    /**
     * @desc 字符串判断非空
     * @author BSW
     * @create 2017-09-21
     * @param sourceStr
     * @return
     */
    public static boolean isNotEmpty(String sourceStr){
        boolean result = true;
        if(isEmpty(sourceStr)){
            result = false;
        }
        return result;
    }

    /**
     * 根据属性名称拼接 对应的Get或者set方法
     *
     * @param name class 对象内的 属性名称
     * @return String 属性名称对应的Get或者set方法名称
     *
     * @date 2017-08-29
     * @creater bsw
     */
    public static String getFieldMethodNameByType(String methodType,String name) {
        if (!StringUtils.hasLength(name)) {
            return "";
        } else {
            StringBuilder result = new StringBuilder();
            result.append(methodType).append(toUpperCase(name.substring(0, 1))).append(name.substring(1,name.length()));
            return result.toString();
        }
    }

    /**
     * 大写转换
     *
     * @param name
     * @return String 转化后的属性名称
     *
     */
    public static String toUpperCase(String name) {
        return name.toUpperCase(Locale.US);
    }


}
