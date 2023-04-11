package com.example.distributelock.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class RedisLock  {

    @Autowired
    private RedisTemplate redisTemplate;

    private  String key;
    private  String value;
    // 过期时间，单位秒
    private  int expireTime;

    public  RedisLock(RedisTemplate redisTemplate,String key,int expireTime){
        this.redisTemplate=redisTemplate;
        this.key=key;
        this.expireTime=expireTime;
        // 设置value值
        this.value= UUID.randomUUID().toString();
    }

    /**\
     * 获取锁的方法
     * @return
     */
    public  boolean getLock(){
        RedisCallback<Boolean> redisCallback= redisConnection -> {
            // 设置NX
            RedisStringCommands.SetOption setOption=RedisStringCommands.SetOption.ifAbsent();
            // 过期时间
            Expiration expiration=Expiration.seconds(expireTime);
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
        return  lock;
    }


    /**
     * 释放锁
     * @return
     */
    public  boolean unlock(){
        // 释放锁定过程使用lua脚本
        String script=" if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
                "                 return redis.call(\"del\",KEYS[1])\n" +
                "                 else\n" +
                "                 return 0\n" +
                "                 end";
        RedisScript<Boolean> redisScript=RedisScript.of(script,Boolean.class);
        List<String> keys = Arrays.asList(key);

        Boolean  result = (Boolean) redisTemplate.execute(redisScript, keys, value);
        return  result;
    }


}
