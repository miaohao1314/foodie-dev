package com.example.distributelock.service;

import com.example.distributelock.util.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SchedulerService {

    @Autowired
    private RedisTemplate redisTemplate;

    // 设置短信每5秒钟执行一次
    @Scheduled(cron = "0/5 * * * * ?")
    public  void  sendSms(){
        log.info("我进入了短信方法");
        String key="autoSms";
        RedisLock redisLock=new RedisLock(redisTemplate,key,30);
        if(redisLock.getLock()){
            log.info("我进入了锁");
            // 模拟一个处理过程15s
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                boolean unlock = redisLock.unlock();
                log.info("释放锁的结果是： "+unlock);
            }
        }
        log.info("向159 发送短信");
    }
}
