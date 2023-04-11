package com.example.distributezklock;


import com.example.distributezklock.lock.ZkLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



/**
 * 测试程序类
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DistributeZkLockApplication {


    @Test
    public  void  testZkLock() throws Exception {
        ZkLock zkLock=new ZkLock();
        // 获取锁
        boolean order = zkLock.getlock("order");
        log.info("获取锁的结果"+order);
        zkLock.close();

    }

}
