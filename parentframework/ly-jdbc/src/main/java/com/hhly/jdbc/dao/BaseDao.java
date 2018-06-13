package com.hhly.jdbc.dao;

import com.hhly.jdbc.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * @author wangxianchen
 * @create 2017-08-22
 * @desc 命名规范:
 *      新增以  insert 开头
 *      更新以  update 开头
 *      删除以  delete 开头
 *      查询以  select 开头
 *   子类的扩展请遵循此约束
 */
public interface BaseDao<T extends BaseEntity> {
    /**
     * @desc 增加一条记录,除约束外的字段可为空.建议使用mapper里的insertSelective
     * @author wangxianchen
     * @create 2017-08-22
     * @param t
     * @return
     */
    boolean insertSelective(T t);

    /**
     * @desc 根据主键选择性更新一条记录.建议使用mapper里的updateByPrimaryKeySelective
     * @author wangxianchen
     * @create 2017-08-22
     * @param t
     * @return
     */
    boolean updateByPrimaryKeySelective(T t);

    /**
     * @desc 根据主键物理删除一条记录
     * @author wangxianchen
     * @create 2017-08-22
     * @param id
     * @return
     */
    boolean deleteByPrimaryKey(Serializable id);

    /**
     * @desc 根据主键查询一条记录,建议使用mapper里的selectByPrimaryKey
     * @author wangxianchen
     * @create 2017-08-22
     * @param id
     * @return
     */
    T selectByPrimaryKey(Serializable id);


    /**
     * @desc 根据惟一字段查询一条记录,建议使用mapper里的selectSelective
     * @author wangxianchen
     * @create 2017-08-22
     * @param t
     * @return
     */
    T selectOneSelective(T t);

    /**
     * @desc 根据一个或多个字段查询多条记录,建议使用mapper里的selectSelective
     * @author wangxianchen
     * @create 2017-08-22
     * @param t
     * @return
     */
    List<T> selectManySelective(T t);

    /**
     * @desc 默认主键生成策略 UUID
     * @author wangxianchen
     * @create 2017-08-24
     * @return
     */
    default String generatePrimaryKey(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
