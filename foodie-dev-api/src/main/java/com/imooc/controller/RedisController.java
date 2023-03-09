package com.imooc.controller;

import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

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
}