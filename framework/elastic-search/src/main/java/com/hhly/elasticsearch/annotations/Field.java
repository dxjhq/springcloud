package com.hhly.elasticsearch.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wangxianchen
 * @create 2017-10-18
 * @desc 文档字段解释器
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
@Inherited
public @interface Field {

    FieldType type() default FieldType.Auto; //文档类型,当为auto时会根据实际类型进行匹配

    FieldIndex index() default FieldIndex.not_analyzed; //分词方式

    DateFormat format() default DateFormat.yyyyMMddHHmmss; //时间格式

    //String pattern() default "";

    boolean store() default false; //是否存储

    boolean fielddata() default false;

    String searchAnalyzer() default "ik_max_word"; //搜索词的分词器 ik_max_word分词器是插件ik提供的，可以对文本进行最大数量的分词

    String analyzer() default "ik_max_word";  //字段文本的分词器,在内容创建时分词

    boolean includeInAll() default false;  //索引字段内容是否加入到_all域中

    //String[] ignoreFields() default {};

   // boolean includeInParent() default false;
}
