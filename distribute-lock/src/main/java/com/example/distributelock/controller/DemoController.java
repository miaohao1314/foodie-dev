package com.example.distributelock.controller;

import com.example.distributelock.dao.DistributeLockMapper;
import com.example.distributelock.model.DistributeLock;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//lombok提供日志的写法：   @Slf4j

@RestController
@Slf4j
public class DemoController {

//    private Lock lock=new ReentrantLock();
//    @Transactional: 代表监听回滚时所发生的异常（noRollbackFor = Exception.class： 代表可以监听到自己手动的异常
//    ，这样达到效果，第一个进程查询完之后会锁住程序，第二个进行便查询不到进行会抛出错误）
    // postman同时开启多个进程进程测试，每个进程要求端口不一样

    @Autowired
    private DistributeLockMapper distributeLockMapper;

    @RequestMapping("singleLocks")
    @Transactional(noRollbackFor = Exception.class)
    public String singleLock() throws Exception {
        log.info("我进入了方法");
        DistributeLock distributeDemo = distributeLockMapper.selectDistributeLock("demo");
        if (distributeDemo==null) throw  new Exception("找不到锁");
        log.info("我进入了锁");
        // 线程休眠60s
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return  "我已经执行完成了";
    }

}
