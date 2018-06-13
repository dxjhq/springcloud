package com.hhly.elasticsearch.annotations;

/**
 * @author wangxianchen
 * @create 2017-10-18
 * @desc
 */
public enum FieldIndex {

    //首先分析这个字符串，然后索引。换言之，以全文形式索引此字段。
    not_analyzed,

    //索引这个字段，使之可以被搜索，但是索引内容和指定值一样。不分析此字段。
    analyzed,

    //不索引这个字段。这个字段不能被搜索到。
    no

}
