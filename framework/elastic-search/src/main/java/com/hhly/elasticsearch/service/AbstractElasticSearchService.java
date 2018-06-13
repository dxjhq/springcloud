package com.hhly.elasticsearch.service;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hhly.elasticsearch.annotations.Document;
import com.hhly.elasticsearch.annotations.FieldType;
import com.hhly.elasticsearch.model.EsBaseModel;
import com.hhly.elasticsearch.model.EsPage;
import org.apache.commons.collections.MapUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexAction;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequestBuilder;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wangxianchen
 * @create 2017-10-13
 * @desc ES通用查询接口之默认实现
 */

public abstract class AbstractElasticSearchService<E extends EsBaseModel> implements BaseElasticSearchService<E>,InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(AbstractElasticSearchService.class);

    @Autowired
    protected TransportClient client;

    protected Document document;

    protected Gson gson;

    protected Class<E> cls;

    @Override
    public void afterPropertiesSet() throws Exception {
        document = getDocument();
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create(); //按照 yyyy-MM-dd HH:mm:ss格式化。
        cls = getEntityClass();
        initIndex();
    }


    /**
     * @desc  新增
     * @author wangxianchen
     * @create 2017-10-14
     * @param e
     * @return
     */
    @Override
    public boolean insert(E e) {
        if(StringUtils.isEmpty(e.getId())){
            e.setId(generatePrimaryKey());
        }
        IndexResponse response = client.prepareIndex(document.indexName(),document.type()).setSource(gson.toJson(e),XContentType.JSON).setId(e.getId()).get();
        if(response != null && response.getResult().equals(DocWriteResponse.Result.CREATED)){
            return true;
        }
        return false;
    }

    /**
     * @desc 批量新增
     * @author wangxianchen
     * @create 2017-10-14
     * @param list
     * @return
     */
    @Override
    public long insert(List<E> list) {
        int success = 0;
        BulkRequestBuilder bulk = client.prepareBulk();
        for(E e : list){
            if(StringUtils.isEmpty(e.getId())){
                e.setId(generatePrimaryKey());
            }
            IndexRequest indexRequest = new IndexRequest(document.indexName(),document.type(), e.getId()).source(gson.toJson(e),XContentType.JSON);
            bulk.add(indexRequest.id(e.getId()));
        }
        BulkResponse bulkResponse = bulk.get();
        if(bulkResponse.hasFailures()){
            BulkItemResponse[] items = bulkResponse.getItems();
            for(BulkItemResponse item : items){
                logger.error("批量操作时失败的记录",item.getFailureMessage());

            }
        }else{
            success = list.size();
        }
        return success;
    }

    /**
     * @desc 更新
     * @author wangxianchen
     * @create 2017-10-14
     * @param e
     * @return
     */
    @Override
    public boolean update(E e)  throws Exception{
        return updateByMap(beanToMap(e));
    }

    /**
     * @desc 根据map更新
     * @author wangxianchen
     * @create 2017-10-17
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public boolean updateByMap(Map<String,Object> map) throws Exception{
        Object id = map.get(EsBaseModel.PRIMARY_KEY);
        assert id != null : "ID不能为空";
        UpdateResponse response = client.prepareUpdate(document.indexName(),document.type(),String.valueOf(id)).setDoc(gson.toJson(map), XContentType.JSON).get();
        if(response != null && response.getResult().equals(DocWriteResponse.Result.UPDATED)){
            return true;
        }
        return false;
    }

    /**
     * @desc 批量更新
     * @author wangxianchen
     * @create 2017-10-14
     * @param list
     * @return
     */
    @Override
    public long update(List<E> list) throws Exception {
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(E e : list){
            mapList.add(beanToMap(e));
        }
        return updateByMapList(mapList);
    }

    /**
     * @desc 根据map批量更新
     * @author wangxianchen
     * @create 2017-10-17
     * @param mapList
     * @return
     * @throws Exception
     */
    @Override
    public long updateByMapList(List<Map<String,Object>> mapList) throws Exception{
        int success = 0;
        BulkRequestBuilder bulk = client.prepareBulk();
        for(Map<String,Object> map : mapList){
            Object id = map.get(EsBaseModel.PRIMARY_KEY);
            assert  id != null : "ID不能为空";
            UpdateRequest updateRequest = new UpdateRequest(document.indexName(),document.type(), String.valueOf(id)).doc(gson.toJson(map),XContentType.JSON);
            bulk.add(updateRequest);
        }
        BulkResponse bulkResponse = bulk.get();
        if(bulkResponse.hasFailures()){
            BulkItemResponse[] items = bulkResponse.getItems();
            for(BulkItemResponse item : items){
                logger.error("批量更新时失败的记录",item.getFailureMessage());
            }
        }else{
            success = mapList.size();
        }

        return success;
    }
    /**
     * @desc 根据ID删除
     * @author wangxianchen
     * @create 2017-10-14
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(String id) {
        assert id != null : "ID不能为空";
        DeleteResponse response = client.prepareDelete(document.indexName(),document.type(),id).get();
        if(response != null && response.getResult().equals(DocWriteResponse.Result.DELETED)){
            return true;
        }
        return false;
    }

    /**
     * @desc 根据ID批量删除
     * @author wangxianchen
     * @create 2017-10-14
     * @param ids
     * @return
     */
    @Override
    public long deleteById(String[] ids) {
        int success = 0;
        BulkRequestBuilder bulk = client.prepareBulk();
        for(String id : ids){
            DeleteRequest deleteRequest = new DeleteRequest(document.indexName(),document.type(), id);
            bulk.add(deleteRequest);
        }
        BulkResponse bulkResponse = bulk.get();
        if(bulkResponse.hasFailures()){
            BulkItemResponse[] items = bulkResponse.getItems();
            for(BulkItemResponse item : items){
                logger.error("批量操作时失败的记录",item.getFailureMessage());
            }
        }else{
            success = ids.length;
        }
        return success;
    }

    /**
     * @desc 删除当前index下的所有数据,请谨慎操作
     * @author wangxianchen
     * @create 2017-10-14
     * @return
     */
    @Override
    public boolean deleteByIndex(){
        if(indexExists()){
            DeleteIndexResponse response = client.admin().indices().prepareDelete(document.indexName()).execute().actionGet();
            return response.isAcknowledged();
        }
        return false;
    }

    /**
     * @desc 删除当前index下的所有type数据
     * @author wangxianchen
     * @create 2017-10-15
     */
    @Override
    public boolean deleteByType() {
        if(typesExists()){
            QueryBuilder builder = QueryBuilders.typeQuery(document.type());//查询整个type
            BulkByScrollResponse response =  DeleteByQueryAction.INSTANCE.newRequestBuilder(client).source(document.indexName()).filter(builder).execute().actionGet();
            if(response != null && response.getDeleted()>0 ){
                return true;
            }
        }
        return false;
    }

    /**
     * @desc 根据ID查询
     * @author wangxianchen
     * @create 2017-10-14
     * @param id
     * @return
     */
    @Override
    public E queryById(String id) {
        assert id != null : "ID不能为空";
        GetResponse getResponse = client.prepareGet(
                document.indexName(),
                document.type(),
                id).get();
        return gson.fromJson(getResponse.getSourceAsString(),cls);
    }

    /**
     * @desc 根据字段名查询
     * @author wangxianchen
     * @create 2017-10-14
     * @param fieldName
     * @param value
     * @return
     */
    @Override
    public List<E> queryByField(String fieldName, Object value){
        List<E> list = null;
        QueryBuilder matchQuery = QueryBuilders.matchQuery(fieldName, value);
        SearchResponse response = client.prepareSearch(document.indexName()).setTypes(document.type()).setQuery(matchQuery).get();
        SearchHits searchHits = response.getHits();
        if(searchHits.getTotalHits() > 0){
            list = new ArrayList<>();
            for(SearchHit hit:searchHits){
                list.add(hit.getField(fieldName).getValue());
            }
        }
        return list;
    }


    /**
     * @desc 判断当前索引是否存在
     * @author wangxianchen
     * @create 2017-10-15
     * @return
     */
    @Override
    public boolean indexExists(){
        IndicesExistsRequest ier = new IndicesExistsRequest(document.indexName());
        return client.admin().indices().exists(ier).actionGet().isExists();
    }


    /**
     * @desc 在当前index下,判断指定type是否存在
     * @author wangxianchen
     * @create 2017-10-15
     * @param indexType
     * @return
     */
    @Override
    public boolean typesExists(String ... indexType){
        if(indexExists()) {
            TypesExistsRequest ter = new TypesExistsRequest(new String[]{document.indexName()}, indexType);
            return client.admin().indices().typesExists(ter).actionGet().isExists();
        }
        return false;
    }

    /**
     * @desc 在当前index下,判断type是否存在
     * @author wangxianchen
     * @create 2017-10-15
     * @return
     */
    @Override
    public boolean typesExists(){
        if(indexExists()) {
            TypesExistsRequest ter = new TypesExistsRequest(new String[]{document.indexName()}, document.type());
            return client.admin().indices().typesExists(ter).actionGet().isExists();
        }
        return false;
    }

    /**
     * @desc 1.根据多个字段或关键字,进行高亮,排序,分页查询.
     *       2.当多个字段未指定时则按关键字查询,如若关键字为空,则查询所有
     *       3.如未指定分页时,则按ES默认的分页限制查询.
     * @author wangxianchen
     * @create 2017-10-14
     * @param filedContentMap
     * @param keyword
     * @param heightFieldList
     * @param fieldSortBuilders
     * @param esPage
     * @return
     */
    @Override
    public PageList<E> queryListWithPage(Map<String, Object> filedContentMap,
                                 String keyword,
                                 final List<String> heightFieldList,
                                 FieldSortBuilder[] fieldSortBuilders,
                                         EsPage esPage) throws Exception {
        //assert paginator != null : "分页参数不能为空";
        if(!indexExists()){
            return new PageList<>( new Paginator( 1, 10, 0 ) );
        }
        HighlightBuilder hibuilder = null;
        //高亮字段
        if(heightFieldList != null && heightFieldList.size() > 0){
            hibuilder = new HighlightBuilder();
            for (String fieldName : heightFieldList) {
                hibuilder.preTags("<em class='heightField'>");
                hibuilder.postTags("</em>");
                hibuilder.field(fieldName);
                hibuilder.fragmentSize(550);
                hibuilder.requireFieldMatch(false);
            }
        }
        //构造查询器
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(document.indexName()).setTypes(document.type());

        searchRequestBuilder.setSearchType(SearchType.QUERY_THEN_FETCH);
        //分页
        if(esPage != null){
            searchRequestBuilder.setFrom(esPage.getFrom()).setSize(esPage.getSize());//分页
        }else{
            esPage = new EsPage();
        }
        //排序
        if (fieldSortBuilders != null){
            for(FieldSortBuilder fsb : fieldSortBuilders){
                searchRequestBuilder.addSort(fsb);
            }
        }
        //字段查询
        if(MapUtils.isNotEmpty(filedContentMap)){
            BoolQueryBuilder qb = QueryBuilders. boolQuery();
            for (String key : filedContentMap.keySet()) {
                int index = key.indexOf(".");
                //嵌套查询
                if(index != -1){
                    qb.must(QueryBuilders.nestedQuery(key.substring(0,index),QueryBuilders.matchQuery(key,filedContentMap.get(key)), ScoreMode.None));
                }else{
                    qb.must(QueryBuilders.matchQuery(key,filedContentMap.get(key)));
                }
            }
            searchRequestBuilder.setQuery(qb);
        }else{
            //关键字不为空,按字符串查询
            if(!StringUtils.isEmpty(keyword)){
                QueryStringQueryBuilder builder = new QueryStringQueryBuilder(keyword);
                //builder.analyzer("ik_max_word");
                builder.useAllFields(true);
                searchRequestBuilder.setQuery(builder);
            }else{
                //通配查询所有
                searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery());
            }
        }
        //高亮
        searchRequestBuilder.highlighter(hibuilder);
        SearchResponse searchResponse = searchRequestBuilder.execute().get();
        List<E> chunk = new ArrayList<E>();
        SearchHits sh = searchResponse.getHits();
        long totalRecords = sh.getTotalHits();
        if(totalRecords >0 ){
            for (SearchHit hit : sh) {
                if(heightFieldList != null && heightFieldList.size()>0){
                    Map<String, Object> entityMap = hit.getSource();
                    for (String highName : heightFieldList) {
                        HighlightField field =  hit.getHighlightFields().get(highName);
                        //获取高亮字段内容
                        if(field != null){
                            Text text[] = field.fragments();
                            if(text.length>0){
                                String highValue = text[0].toString();
                                entityMap.put(highName, highValue);
                            }
                        }
                    }
                    try {
                        chunk.add((E) this.mapToBean(entityMap,cls,null));
                    } catch (Exception e) {
                        logger.error("进行高亮字段处理时报错",e);
                    }
                }else{
                    chunk.add(gson.fromJson(hit.getSourceAsString(),cls));
                }
            }
        }
        PageList<E> pageList = new PageList(chunk,new Paginator(esPage.getPage(),esPage.getSize(), (int) totalRecords));
        return pageList;
    }


    /**
     * @desc Map转成javaBean对象
     * @author wangxianchen
     * @create 2017-10-14
     * @param map
     * @param beanClass
     * @param innerName
     * @return
     * @throws Exception
     */
    protected Object mapToBean(Map<String, Object> map, Class<?> beanClass,String innerName) throws Exception {
        if (MapUtils.isEmpty(map)){
            return null;
        }
        Object obj = beanClass.newInstance();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            Method setter = property.getWriteMethod();
            //String innerName = property.getName();
            if(EsBaseModel.class.getName().equals(property.getPropertyType().getSuperclass().getName())){
                Object innerObj =  mapToBean(map, property.getPropertyType(),property.getName());
                if(setter != null){
                    setter.invoke(obj,innerObj);
                }
            }else if (setter != null) {
                Object v = null;
                if(innerName != null){
                    v = map.get(innerName+"."+property.getName());
                    //System.out.println(innerName+"."+property.getName() +"="+v+"---"+map.get(innerName)+"-----"+map.get(property.getName()));
                    v = v == null ? ((Map)map.get(innerName)).get(property.getName()): v;
                }else{
                    v = map.get(property.getName());
                }
                //Object v = innerName != null ? map.get(innerName+"."+property.getName()) : map.get(property.getName());
                //      v = v == null ? map.get(property.getName()) : v;
                setter.invoke(obj,convert(property.getPropertyType(),v));
            }
        }
        return obj;
    }

    /**
     * @desc java转map,去掉了空值
     * @author wangxianchen
     * @create 2017-10-17
     * @param bean
     * @return
     * @throws Exception
     */
    protected  Map<String,Object> beanToMap(Object bean) throws Exception{
        if(bean == null){
            return null;
        }
        Map<String,Object> map = new HashMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            Method getter = property.getReadMethod();
            String propertyName = property.getName();
            Object value = getter.invoke(bean);
            if(value != null && !propertyName.equals("class")){
                map.put(propertyName,value);
            }
        }
        return map;
    }

    /**
     * @desc 转换成对应属性类型的值
     * @author wangxianchen
     * @create 2017-10-14
     * @param cls
     * @param value
     * @return
     */
    protected Object convert(Class<?> cls,Object value){
        if(Long.class.getName().equalsIgnoreCase(cls.getName())){
            return Long.valueOf(value.toString());
        }else if(Date.class.getName().equalsIgnoreCase(cls.getName())){
            if(value!=null){
                Date date = null;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    date = sdf.parse(value.toString());
                } catch (ParseException e) {
                    logger.error("parse date error",value,e);
                }
                return date;
            }
        }
        return value;
    }

    /**
     * @desc 创建索引,setting,mapping,如果存在则不创建
     * @author wangxianchen
     * @create 2017-10-15
     * @return
     * @throws Exception
     */
    private void initIndex() throws Exception {
        if(!document.createIndex()){
            return;
        }
        if(indexExists()) {
            OpenIndexRequestBuilder openIndexBuilder = new OpenIndexRequestBuilder(client.admin().indices(),OpenIndexAction.INSTANCE);
            openIndexBuilder.setIndices(document.indexName()).execute().actionGet();
        }else{
            CreateIndexRequestBuilder prepareCreate = client.admin().indices().prepareCreate(document.indexName());
            prepareCreate.setSettings(createSetting());
            prepareCreate.execute().actionGet();
        }
        //如果该type存在
        if(typesExists()) {
            return;
        }

        PutMappingRequest mappingRequest = Requests.putMappingRequest(document.indexName()).type(document.type()).source(createMapping());
        client.admin().indices().putMapping(mappingRequest).actionGet();
    }

    /**
     * @desc 创建mapping映射 子类可以自由实现
     * @author wangxianchen
     * @create 2017-10-15
     * @return
     * @throws Exception
     */
    @Override
    public XContentBuilder createMapping() throws Exception{
        XContentBuilder mapping = JsonXContent.contentBuilder();
        mapping.startObject();
        mapping.startObject(document.type());
        this.assemblyMapping(mapping,cls);
        mapping.endObject();
        mapping.endObject();
        //System.out.println(mapping.string());
        return mapping;
    }

    /**
     * @desc 创建setting 子类可以自由实现
     * @author wangxianchen
     * @create 2017-10-15
     * @return
     * @throws Exception
     */
    @Override
    public XContentBuilder createSetting() throws Exception{
        XContentBuilder contentBuilder = JsonXContent.contentBuilder();
        contentBuilder.startObject();
        contentBuilder.startObject("index")
                .field("refresh_interval",document.refreshInterval())
                .field("number_of_shards",document.shards())
                .field("number_of_replicas",document.replicas())
                .startObject("store").field("type",document.indexStoreType()).endObject()
                .endObject();
        contentBuilder.endObject();
        return contentBuilder;
    }

    /**
     * @desc 组装mapping json
     * @author wangxianchen
     * @create 2017-10-15
     * @param mapping
     * @param clz
     * @throws Exception
     */
    private void assemblyMapping(XContentBuilder mapping,Class<?> clz) throws Exception {
        Object obj = clz.newInstance();
        Field[] fields = obj.getClass().getDeclaredFields();
        mapping.startObject("properties");
        for(Field f : fields){
            com.hhly.elasticsearch.annotations.Field fieldAnnotation = f.getAnnotation(com.hhly.elasticsearch.annotations.Field.class);
            if(fieldAnnotation != null) {
                mapping.startObject(f.getName());
                FieldType fieldType = fieldAnnotation.type();
                if (fieldType.equals(FieldType.Auto)) {
                    fieldType = getFieldType(f.getType().getName());
                }
                mapping.field("type", fieldType.name().toLowerCase());
                if(EsBaseModel.class.getName().equals(f.getType().getSuperclass().getName())){
                    assemblyMapping(mapping,f.getType());
                    mapping.endObject();
                    continue;
                }

                mapping.field("index",fieldAnnotation.index());
                mapping.field("store",fieldAnnotation.store());
                mapping.field("include_in_all",fieldAnnotation.includeInAll());
                if(fieldType.equals(FieldType.Text)){
                    mapping.field("analyzer", fieldAnnotation.analyzer());
                    mapping.field("search_analyzer", fieldAnnotation.searchAnalyzer());
                    mapping.field("fielddata", fieldAnnotation.fielddata());
                }
                if(fieldType.equals(FieldType.Date)){
                    mapping.field("format", fieldAnnotation.format().getLabel());
                }
                mapping.endObject();
            }
        }
        mapping.endObject();
    }

    /**
     * @desc 莸属性类型
     * @author wangxianchen
     * @create 2017-10-18
     * @param propertyTypeName
     * @return
     */
    private FieldType getFieldType(String propertyTypeName){
        FieldType fieldType;
        if(Byte.class.getName().equalsIgnoreCase(propertyTypeName)){
            fieldType = FieldType.Byte;

        }else if(Short.class.getName().equalsIgnoreCase(propertyTypeName)){
            fieldType = FieldType.Short;

        }else if(Integer.class.getName().equalsIgnoreCase(propertyTypeName)){
            fieldType = FieldType.Integer;

        }else if(Long.class.getName().equalsIgnoreCase(propertyTypeName)){
            fieldType = FieldType.Long;

        }else if(Float.class.getName().equalsIgnoreCase(propertyTypeName)){
            fieldType = FieldType.Float;

        }else if(Double.class.getName().equalsIgnoreCase(propertyTypeName)){
            fieldType = FieldType.Double;

        }else if(Boolean.class.getName().equalsIgnoreCase(propertyTypeName)){
            fieldType = FieldType.Boolean;

        }else if(Date.class.getName().equalsIgnoreCase(propertyTypeName)){
            fieldType = FieldType.Date;

        }else if(String.class.getName().equalsIgnoreCase(propertyTypeName)){
            fieldType = FieldType.Text;
        }else{
            fieldType = FieldType.Object;
        }
        return fieldType;
    }
}
