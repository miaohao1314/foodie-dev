package com.test;


import com.imooc.Application;
import com.imooc.es.pojo.Stu;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ESTest {
    /**
     * es： 数据库
     * 里面数据是jsong，json对应实体的一个对象，是一个entity
     */

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public  void  createIndesStu(){

        Stu stu=new Stu();
        stu.setStuId(1001L);
        stu.setName("bat man");
        stu.setAge(18)  ;
        stu.setMoney(18.8f);
        stu.setSign("i am spider man");
        stu.setDescription("i wish i am spilder man");

        IndexQuery build = new IndexQueryBuilder().withObject(stu).build();
        // 创建索引
        elasticsearchTemplate.index(build);

    }
}
