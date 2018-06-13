package com.hhly.jdbc.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangxianchen
 * @create 2017-08-22
 * @desc 基类,所有的数据表实体必需有以下字段,也必需继承此类
 */

@Getter
@Setter
@ToString
public class BaseEntity implements Serializable {

    /** 数据库自增ID **/
    private Integer id;

    /**业务主键UUID,惟一,长32位**/
    private String uuid;

    /** 创建时间 **/
    private Date createTime;

    /** 创建人ID **/
    private String createUser;

    /** 更新时间 **/
    private Date updateTime;

    /** 更新人ID **/
    private String updateUser;

    /** 删除标识(0:未删除 1:已删除) **/
    private Byte isDelete;
}
