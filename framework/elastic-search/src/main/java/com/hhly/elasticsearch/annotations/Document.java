package com.hhly.elasticsearch.annotations;

import java.lang.annotation.*;

/**
 * @author wangxianchen
 * @create 2017-10-13
 * @desc ES文档设置.因springboot暂不支持5.x.x的版本,故将springboot的这种对文档设置的方式移植了出来,重新做了实现
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Document {

    /**
     *索引名称 相当于Database
     */
    String indexName();

    /**
     * 文档名称 相当于Table
     */
    String type() default "";

    /**
     * 这个属性暂未用
     */
    boolean useServerConfiguration() default false;

    /**
     * 代表索引分片，es可以把一个完整的索引分成多个分片，这样的好处是可以把一个大的索引拆分成多个，分布到不同的节点上。
     * 构成分布式搜索。分片的数量只能在索引创建前指定，并且索引创建后不能更改。
     * 默认 5
     */
    short shards() default 5;

    /**
     * 代表索引副本，es可以设置多个索引的副本，副本的作用一是提高系统的容错性，
     * 当个某个节点某个分片损坏或丢失时可以从副本中恢复。二是提高es的查询效率，es会自动对搜索请求进行负载均衡
     * 默认 1
     */
    short replicas() default 1;

    /**
     * 刷新索引文件块的间隔时间,
     * 默认为1秒,禁用为-1
     */
    String refreshInterval() default "1s";

    /**
     *存储系统类型
     */
    String indexStoreType() default "fs";

    /**
     * 是否创建索引,是的话会在spring加载BaseElasticSearchService时创建,如果索引已存在则不创建
     */
    boolean createIndex() default true;

}