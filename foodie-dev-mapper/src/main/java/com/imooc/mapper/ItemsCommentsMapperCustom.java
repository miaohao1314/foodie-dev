package com.imooc.mapper;

import com.imooc.my.mapper.MyMapper;
import com.imooc.pojo.ItemsComments;
import com.imooc.pojo.vo.MyCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapperCustom extends MyMapper<ItemsComments> {

    public void saveComments(Map<String, Object> map);

    // @Param: 这里定义的名称会在ItemsCommentsMapperCustom.xml中进行引用
    public List<MyCommentVO> queryMyComments(@Param("paramsMap") Map<String, Object> map);

}