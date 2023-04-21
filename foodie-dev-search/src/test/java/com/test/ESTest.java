package com.test;


import com.imooc.Application;
import com.imooc.es.pojo.Stu;
import org.elasticsearch.action.index.IndexRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ESTest {
    /**
     * es： 数据库
     * 里面数据是jsong，json对应实体的一个对象，是一个entity
     * * 不建议使用 ElasticsearchTemplate 对索引进行管理（创建索引，更新映射，删除索引）
     * * 索引就像是数据库或者数据库中的表，我们平时是不会是通过java代码频繁的去创建修改删除数据库或者表的
     * * 我们只会针对数据做CRUD的操作
     * * 在es中也是同理，我们尽量使用 ElasticsearchTemplate 对文档数据做CRUD的操作
     * * 1. 属性（FieldType）类型不灵活
     * * 2. 主分片与副本分片数无法设置
     */

    @Autowired
    private ElasticsearchTemplate esTemplate;

    /**
     * 创建一个索引
     */
    @Test
    public void createIndesStu() {

        Stu stu = new Stu();
        stu.setStuId(1001L);
        stu.setName("bat man");
        stu.setAge(18);
        stu.setMoney(18.8f);
        stu.setSign("i am spider man");
        stu.setDescription("i wish i am spilder man");

        IndexQuery build = new IndexQueryBuilder().withObject(stu).build();
        // 创建索引
        esTemplate.index(build);
    }

    /**
     * 删除一个索引
     */

    @Test
    public void deleteIndexStu() {
        esTemplate.deleteIndex(Stu.class);
    }

    /////////////////////////开始对文档数据进行操作//////////////////////////////////

    /**
     * 对文档数据进行修改操作
     */
    @Test
    public void updateStuDoc() {

        // 设置要更新的数据
        Map<String, Object> sourceMap = new HashMap<>();
        sourceMap.put("sign", "I am not super man");
        sourceMap.put("money", 99.8f);
        sourceMap.put("age", 33);

        IndexRequest indexRequest = new IndexRequest();
        // 具体要更新的数据
        indexRequest.source(sourceMap);

        UpdateQuery updateQuery = new UpdateQueryBuilder()
                .withClass(Stu.class)
                .withId("1001")
                .withIndexRequest(indexRequest)
                .build();

        // 相当于一个sql语句
//        update stu set sign='abc',age=33,money=88.6 where docId='1002'

        esTemplate.update(updateQuery);
    }


    /**
     * 对文档数据进行查询操作
     */

    @Test
    public void getStuDoc() {

        GetQuery query = new GetQuery();
        query.setId("1001");
        Stu stu = esTemplate.queryForObject(query, Stu.class);
        System.out.println(stu);
    }

    /**
     * 对文档数据进行删除操作，不会像索引那样将整合表给删除掉，它只会删除表格里面的数据
     */
    @Test
    public void deleteStuDoc() {
        esTemplate.delete(Stu.class, "1001");
    }


}
