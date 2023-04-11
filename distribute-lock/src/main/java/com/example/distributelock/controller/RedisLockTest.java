package com.example.distributelock.controller;

import com.example.distributelock.util.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * jdk 1.7之后推出了AutoCloseable，可以自动关闭锁
 */


@RestController
@Slf4j
public class RedisLockTest  implements  AutoCloseable {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("RedisLockTest")
    public  String redisLock(){
        log.info("我进入了方法");

        // 设置key
        String key="redisKey";
        RedisLock redisLock=new RedisLock(redisTemplate,key,30);

        if(redisLock.getLock()){
            log.info("我进入了锁");
            // 模拟一个处理过程15s
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("方法执行完成");
        return  "方法执行完成";

    }
    @Override
    public void close() throws Exception {
//        unLock();
    }

}
