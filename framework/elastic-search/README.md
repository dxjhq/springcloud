ElasticSearch整合
---

 **version** 1.0
 
**一.设计思路**

 - ElasticSearch是一个基于Lucene的搜索服务器。它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口
 - springboot暂不支持5.0+版本的ElasticSearch。此次整合是针对ES v5.5.2进行的基础封装
 - 该模块集成了最基础的CRUD操作,利用注解可自动化实现数据模型映射到ES中,可初始化每个模型的配置信息
 - 操作简单,在POM中引入该模块,并在boot启动类中引入ES配置即可.具体请参考下文中的使用方法
 - [Elasticsearch 权威指南（中文版）](http://www.ctolib.com/docs/sfile/elasticsearch-definitive-guide-cn/index.html)
 

**二.使用方法**

 - POM引入ES依赖
    
       		<dependency>
       			    <groupId>com.hhly</groupId>
       			    <artifactId>elastic-search</artifactId>
       			    <version>3.0.0.1</version>
       		</dependency> 
       		
 - yml文件配置ES连接信息
    
        spring:
          data:
                elasticsearch:
                    cluster-name: elasticsearch
                    cluster-nodes: 192.168.10.224:9300
   
    
 - 创建数据模型类(model)
 
        说明: 
        1.所有model必需extends EsBaseModel. 惟一ID必需指定具体值,如为空的话,系统在保存时会自动指定默认值(UUID)
        2.@Document必需要要有,其中的indexName,type必需要指定,其它值不指定的话均采用默认值 
        3.@Field 非必填.如果填了请务必填写正确
        4.系统在启动时会根据model上的注解进行index,type,settings,mappings创建,如果ES中存在该index和type将不会再创建.
        5.字段如没有@Field注解,系统启动时将不会进行mapping映射. ES会在数据(JSON格式)保存时自动添加当前不存在的字段映射
         
        如下例所示:
        ...
        @Getter
        @Setter
        @ToString
        @Document(indexName="projectname",type="clazz",indexStoreType="fs",shards=5,replicas=1,refreshInterval="1s")
        public class Clazz extends EsBaseModel {
        
            @Field(type = FieldType.Text)
            private String name;
        
            @Field(type = FieldType.Text)
            private String teacher;
        
            @Field(type = FieldType.Date,format= DateFormat.yyyyMMddHHmmss, index= FieldIndex.analyzed)
            private Date createTime;
        }
        ...
        
        

        
 - 创建处理服务(service)
        
        1.本模块已经抽象了一些常用的CURD操作方法,并在AbstractElasticSearchService.java中作了实现
        
        2.通过extends AbstractElasticSearchService可以自由扩展方法
       
        
**三.关于ES-demo**   

  - 演示截图
  - ![image]{http://192.168.10.81/framework/elastic-search-demo/blob/master/screenshots.png}
  
---
@author: wangxianchen

@date:2017/08/18