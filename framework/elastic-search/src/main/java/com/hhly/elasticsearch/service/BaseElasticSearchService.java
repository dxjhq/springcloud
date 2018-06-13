package com.hhly.elasticsearch.service;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.hhly.elasticsearch.annotations.Document;
import com.hhly.elasticsearch.model.EsBaseModel;
import com.hhly.elasticsearch.model.EsPage;
import com.hhly.utils.ValueUtil;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * @author wangxianchen
 * @create 2017-10-13
 * @desc ES通用查询接口
 */
public interface BaseElasticSearchService<E extends EsBaseModel> {

    default Document getDocument(){
        return getEntityClass().getAnnotation(Document.class);
    }

    default String generatePrimaryKey(){
        return ValueUtil.uuid();
    }

    default Class<E> getEntityClass(){
        return (Class<E>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }


    boolean insert(E e);

    long insert(List<E> list);

    boolean update(E e) throws Exception;

    boolean updateByMap(Map<String,Object> map) throws Exception;

    long update(List<E> list) throws Exception;

    long updateByMapList(List<Map<String,Object>> mapList) throws Exception;

    boolean deleteById(String id);

    long deleteById(String[] ids);

    boolean deleteByIndex();

    boolean deleteByType();

    boolean indexExists();

    boolean typesExists(String ... indexType);

    boolean typesExists();

    E queryById(String id);

    List<E> queryByField(String fieldName, Object value);

    PageList<E> queryListWithPage(Map<String, Object> filedContentMap,
                                  String keyword,
                                  final List<String> heightFieldList,
                                  FieldSortBuilder[] fieldSortBuilders,
                                  EsPage esPage) throws ExecutionException, InterruptedException, Exception;

    XContentBuilder createMapping() throws Exception;

    XContentBuilder createSetting() throws Exception;
}
