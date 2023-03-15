package com.imooc.controller;

import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApiIgnore
@RestController
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;

    // 引入redis的工具类
    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/set")
    public Object set(String key, String value) {
//        redisTemplate.opsForValue().set(key, value);
        // 调用工具类进行存储
        redisOperator.set(key,value);
        return "OK";
    }

    @GetMapping("/get")
    public String get(String key) {
//        return (String)redisTemplate.opsForValue().get(key);
        return redisOperator.get(key);
    }


    @GetMapping("/delete")
    public Object delete(String key) {
//        redisTemplate.delete(key);
        redisOperator.del(key);
        return "OK";
    }

    /**
     * 大量key的查询
     * @return
     */
    @GetMapping("/getALot")
    public  Object getALot(String... keys){
        // 将for循环中返回的值封装在list集合中
        List<String> resutl=new ArrayList<>();
        for (String key : keys) {
            resutl.add(redisOperator.get(key));
        }
        return resutl;
    }

    /**
     * 批量查询mget
     * @return
     * http://127.0.0.1:8088/redis/mget?keys=a1,c1
     */
    @GetMapping("/mget")
    public  Object mget(String... keys){
        // 将数组转化为list
        List<String> keyList= Arrays.asList(keys);
        return  redisOperator.mget(keyList);
    }











}