package com.example.distributelock.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class RedisLockController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("redisLock")
    public  String redisLock(){
        log.info("我进入了方法");
        // 设置key
        String key="redisKey";
        // 设置value值
        String value= UUID.randomUUID().toString();

        RedisCallback<Boolean> redisCallback=redisConnection -> {
            // 设置NX
            RedisStringCommands.SetOption setOption=RedisStringCommands.SetOption.ifAbsent();
            // 过期时间,设置为30s
            Expiration expiration=Expiration.seconds(30);
            // 将key值转化为byte类型
            byte [] redisKey=redisTemplate.getKeySerializer().serialize(key);
            // 序列化value
            byte [] redisValue = redisTemplate.getValueSerializer().serialize(value);
            // 执行setnx操作
            Boolean result = redisConnection.set(redisKey,redisValue,expiration,setOption);
            return  result;
        };

        // 获取分布式锁
        Boolean lock = (Boolean) redisTemplate.execute(redisCallback);

        if(lock){
            log.info("我进入了锁");
            // 模拟一个处理过程15s
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                // 释放锁定过程使用lua脚本
                String script=" if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
                        "                 return redis.call(\"del\",KEYS[1])\n" +
                        "                 else\n" +
                        "                 return 0\n" +
                        "                 end";
                RedisScript<Boolean> redisScript=RedisScript.of(script,Boolean.class);
                List<String> keys = Arrays.asList(key);

                Boolean  result = (Boolean) redisTemplate.execute(redisScript, keys, value);
                log.info("释放锁的结果是： "+result);
            }
        }
        log.info("方法执行完成");
        return  "方法执行完成";
    }
}
