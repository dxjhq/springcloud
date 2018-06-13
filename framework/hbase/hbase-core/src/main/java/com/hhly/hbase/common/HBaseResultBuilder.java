package com.hhly.hbase.common;

import org.apache.commons.collections.CollectionUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.logging.Logger;

public class HBaseResultBuilder<T> {

  private static final Logger log = Logger.getLogger(HBaseResultBuilder.class.getName());
  //需要转换的PO类 Class对象
  private Class<T> mappedClass;

  private Map<String, PropertyDescriptor> mappedFields;
  private Set<String> mappedProperties;
  HashSet populatedProperties;
  private BeanWrapper beanWrapper;
  private Result result;
  private String columnFamilyName;
  private T t;

  /**
   * 提供默认构造方法
   *
   *
   * @date 2017-08-28
   * @creater bsw
   */
  public HBaseResultBuilder(){}

  /**
   * 初始化参数并实例化要返回的结果对象
   *
   * @param columnFamilyName 列簇名称
   * @param result  Hbase查询结果集
   * @param clazz  结果PO 的Class对象
   *
   * @date 2017-08-28
   * @creater bsw
   */
  public HBaseResultBuilder(String columnFamilyName, Result result, Class<T> clazz) {
    this.columnFamilyName = columnFamilyName;
    this.result = result;
    this.mappedClass = clazz;
    mappedFields = new HashMap<>();
    mappedProperties = new HashSet<>();
    populatedProperties = new HashSet<>();
    this.t = BeanUtils.instantiate(clazz);
    PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
    PropertyDescriptor[] var3 = pds;
    int var4 = pds.length;
    for (int var5 = 0; var5 < var4; ++var5) {
      PropertyDescriptor pd = var3[var5];
      if (pd.getWriteMethod() != null) {
        this.mappedFields.put(this.lowerCaseName(pd.getName()), pd);
        String underscoredName = this.underscoreName(pd.getName());
        if (!this.lowerCaseName(pd.getName()).equals(underscoredName)) {
          this.mappedFields.put(underscoredName, pd);
        }
        this.mappedProperties.add(pd.getName());
      }
    }
    beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(t);
  }

  /**
   * 规范化 实体对象内属性名称
   *     驼峰的加下划线，无驼峰的直接小写
   *
   * @param name 需要格式化的字符串
   * @return String 格式化后的字符串
   */
  private String underscoreName(String name) {
    if (!StringUtils.hasLength(name)) {
      return "";
    } else {
      StringBuilder result = new StringBuilder();
      result.append(this.lowerCaseName(name.substring(0, 1)));

      for (int i = 1; i < name.length(); ++i) {
        String s = name.substring(i, i + 1);
        String slc = this.lowerCaseName(s);
        if (!s.equals(slc)) {
          result.append("_").append(slc);
        } else {
          result.append(s);
        }
      }
      
      return result.toString();
    }
  }

  /**
   * 转换小写
   *
   * @param name
   * @return
   */
  private String lowerCaseName(String name) {
    return name.toLowerCase(Locale.US);
  }

  /**
   * 解析为Java对象处理方法
   *
   * @param columnName
   * @return
   */
  public HBaseResultBuilder build(String columnName) {
    byte[] value = result.getValue(columnFamilyName.getBytes(), columnName.getBytes());
    if (value == null || value.length == 0) {
      return this;
    } else {
      String field = this.lowerCaseName(columnName.replaceAll(" ", ""));
      PropertyDescriptor pd = this.mappedFields.get(field);
      if (pd == null) {
        log.info("HBaseResultBuilder error: can not find property: " + field);
      } else {
        beanWrapper.setPropertyValue(pd.getName(), Bytes.toString(value));
        populatedProperties.add(pd.getName());
      }
    }
    return this;
  }

  /**
   *  伪造Java8的即视感，“流最后的终端操作“。
   *
   *
   */
  public T fetch() {
    //只要有一个属性被解析出来就返回结果对象，毕竟hbase存的是稀疏数据，不一定全量
    if (CollectionUtils.isNotEmpty(populatedProperties)) {
      return this.t;
    } else {
      return null;
    }
  }
}