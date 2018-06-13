package com.hhly.hbase.common;

import org.apache.hadoop.hbase.client.Result;
import org.springframework.data.hadoop.hbase.RowMapper;

public class ConvertRowMapper<T> implements RowMapper<T> {


    @Override
    public T mapRow(Result result, int rowNum) throws Exception {

        return null;
    }
}
