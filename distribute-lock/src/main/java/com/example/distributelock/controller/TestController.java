package com.example.distributelock.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//lombok提供日志的写法：   @Slf4j

@RestController
@Slf4j
public class TestController {

    private Lock lock=new ReentrantLock();
    @RequestMapping("singleLocks")
    public String singleLock(){
        log.info("我进入了方法");
        // 开始执行锁
        lock.lock();
        log.info("我进入了锁");

        // 线程休眠60s
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 释放锁
        lock.unlock();
        return  "我已经执行完成了";
    }

}
