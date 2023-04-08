package com.example;

import com.example.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * order表为空，执行完之后order表会有5条数据
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributeDemoApplicationTests {
    @Autowired
    private OrderService orderService;

    @Test
    public void concurrentOrder() throws InterruptedException {
        Thread.sleep(60000);
        // 闭锁，等待5个线程都执行完，主线程再去关闭
        CountDownLatch cdl = new CountDownLatch(5);
        // 等待5个线程，等待某一个时刻同时并发去执行
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

        // 创建一个线程池，拥有5个线程
        ExecutorService es = Executors.newFixedThreadPool(5);
        for (int i =0;i<5;i++){
            // es.execute(() : 给5个线程各自分配任务
            es.execute(()->{
                try {
                    // cyclicBarrier.await()：  让所有的线程都等待，等待某一个时刻同时并发去执行
                    cyclicBarrier.await();
                    // 执行创建订单的方法

                    Integer orderId = orderService.createOrder();
                    System.out.println("订单id："+orderId);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    cdl.countDown();
                }
            });
        }
        // 闭锁，等待5个线程都执行完，主线程再去关闭
        cdl.await();
        // 5个线程执行完毕，关闭线程池
        es.shutdown();
    }
}