package com.hhly.hbase.dao;

import org.apache.hadoop.hbase.client.Scan;

import java.util.List;

/**
 * @author BSW
 * @create 2017-10-25
 * @desc
 */
public interface HbaseDaoImpl<T> {

    public void put(String tableName,String familyName,String rowKey,T t);

    public T  getObjectByFamily(String t_name,String rowKey,String familyName,Class<T> clz);

    public List<T> getObjectByFamilyAndPage(String t_name, String familyName, Scan scan, Class<T> clz);

    public void deleteRow(String t_name,String rowName,String familyName);

    public void createTable(String t_Name,String ... familyNames);



}
