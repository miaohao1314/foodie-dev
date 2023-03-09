package com.imooc.mapper;

import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
//二级和三级分类
//通用mapper  第一

public interface CategoryMapperCustom {

    public List<CategoryVO> getSubCatList(Integer rootCatId);

//    作为参数传进去的map
    public List<NewItemsVO> getSixNewItemsLazy(@Param("paramsMap") Map<String, Object> map);
}