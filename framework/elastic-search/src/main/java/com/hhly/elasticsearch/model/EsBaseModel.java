package com.hhly.elasticsearch.model;

import com.hhly.elasticsearch.annotations.Field;
import com.hhly.elasticsearch.annotations.FieldIndex;
import com.hhly.elasticsearch.annotations.FieldType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangxianchen
 * @create 2017-10-11
 * @desc ES基础模型
 */

@Getter
@Setter
public class EsBaseModel{

    public static final String PRIMARY_KEY = "id";

    @Field(type= FieldType.Text,index = FieldIndex.analyzed)
    private String id;

}
