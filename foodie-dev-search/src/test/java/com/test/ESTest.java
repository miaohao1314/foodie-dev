package com.test;


import com.imooc.Application;
import com.imooc.es.pojo.Items;
import com.imooc.es.pojo.Stu;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

//        Stu stu = setIndexData();
//
//        IndexQuery build = new IndexQueryBuilder().withObject(stu).build();

        Items items=setIndexDatas();
        IndexQuery build = new IndexQueryBuilder().withObject(items).build();
        // 创建索引
        esTemplate.index(build);
    }


    public Stu setIndexData() {
        Stu stu = new Stu();
        stu.setStuId(1001L);
        stu.setStuId(1002L);
        stu.setName("bat man");
        stu.setName("schesa man");
        stu.setAge(18);
        stu.setAge(20);
        stu.setMoney(18.8f);
        stu.setMoney(29.8f);
        stu.setSign("i am spider man");
        stu.setSign("ffffffff spider man");
        stu.setDescription("i wish i am spilder man");
        stu.setDescription("ffffffffam spilder man");
        return stu;
    }

    /**
     * 为了item表设置数据
     */
    public Items setIndexDatas() {
        Items items=new Items();
        items.setItemId("cake-37");
        items.setItemName("好吃蛋糕");
        items.setPrice(4720);
        items.setImgUrl("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png");
        items.setSellCounts(3786);
        return items;
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
        Map<String, Object> sourceMap = setDocData();

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

    public Map<String, Object> setDocData() {
        Map<String, Object> sourceMap = new HashMap<>();
        sourceMap.put("sign", "I am not super man");
        sourceMap.put("money", 99.8f);
        sourceMap.put("age", 33);
        return sourceMap;
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

    /**
     * 对文档进行分页搜索
     */

    @Test
    public void searchStuDoc() {
//        携带分页的参数,size表示每页显示几条数据
        Pageable pageable = PageRequest.of(0, 3);

//        SearchQuery： 搜索条件
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("description", "man"))
                .withPageable(pageable)
                .build();
        AggregatedPage<Stu> pagedStu = esTemplate.queryForPage(query, Stu.class);
        System.out.println("检索后的总分页数目为：" + pagedStu.getTotalPages());
//        获得分页的内容
        List<Stu> stuList = pagedStu.getContent();
        for (Stu s : stuList) {
            System.out.println(s);
        }
    }



    /**
     * 文档操作高亮显示
     */
    @Test
    public void highlightStuDoc() {
//        标签前置
        String preTag = "<font color='red'>";
//        标签后置
        String postTag = "</font>";

        Pageable pageable = PageRequest.of(0, 10);

//        money倒序
        SortBuilder sortBuilder = new FieldSortBuilder("money")
                .order(SortOrder.DESC);
//        age 排序
        SortBuilder sortBuilderAge = new FieldSortBuilder("age")
                .order(SortOrder.ASC);

//        对查询的词进行高亮展示
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("description", "save man"))
                .withHighlightFields(new HighlightBuilder.Field("description")
                        .preTags(preTag)
                        .postTags(postTag))
                .withSort(sortBuilder)
                .withSort(sortBuilderAge)
                .withPageable(pageable)
                .build();

        AggregatedPage<Stu> pagedStu = esTemplate.queryForPage(query, Stu.class, new SearchResultMapper() {

            // 映射的结果集
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {

                List<Stu> stuListHighlight = new ArrayList<>();

                // 命中
                SearchHits hits = response.getHits();
                for (SearchHit h : hits) {
                    // 拿到高亮的名称
                    HighlightField highlightField = h.getHighlightFields().get("description");
                    String description = highlightField.getFragments()[0].toString();

                    Object stuId = (Object) h.getSourceAsMap().get("stuId");
                    String name = (String) h.getSourceAsMap().get("name");
                    Integer age = (Integer) h.getSourceAsMap().get("age");
                    String sign = (String) h.getSourceAsMap().get("sign");
                    Object money = (Object) h.getSourceAsMap().get("money");

                    Stu stuHL = new Stu();
                    stuHL.setDescription(description);
                    stuHL.setStuId(Long.valueOf(stuId.toString()));
                    stuHL.setName(name);
                    stuHL.setAge(age);
                    stuHL.setSign(sign);
                    stuHL.setMoney(Float.valueOf(money.toString()));

                    stuListHighlight.add(stuHL);
                }

                if (stuListHighlight.size() > 0) {
                    return new AggregatedPageImpl<>((List<T>) stuListHighlight);
                }

                return null;
            }
        });
        System.out.println("检索后的总分页数目为：" + pagedStu.getTotalPages());
        List<Stu> stuList = pagedStu.getContent();
        for (Stu s : stuList) {
            System.out.println(s);
        }
    }
}
