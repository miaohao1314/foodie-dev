package com.imooc.es.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 建立document文档
 */

@Document(indexName = "stu")
@Data
public class Stu {

    @Id
    private Long stuId;

    @Field(store = true)
    private  String name;

    @Field(store = true)
    private  Integer age;

    @Field(store = true)
    private  float  money;

    @Field(store = true, type = FieldType.Keyword)
    private  String sign;

    @Field(store = true)
    private  String description;
}
