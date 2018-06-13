package com.hhly.jdbc.dao;

import com.hhly.jdbc.entity.BaseEntity;
import java.io.Serializable;
import java.util.List;

/**
* @author wangxianchen
* @create 2017-08-24
* @desc 基础通用mapper,扩展方法由子类实现
*/
public interface BaseMapper<T extends BaseEntity> {

    int insertSelective(T t);

    T selectByPrimaryKey(Serializable id);

    int deleteByPrimaryKey(Serializable id);

    int updateByPrimaryKeySelective(T t);

    List<T> selectSelective(T t);
}