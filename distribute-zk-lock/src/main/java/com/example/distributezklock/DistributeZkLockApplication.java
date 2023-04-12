package com.example.distributezklock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DistributeZkLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributeZkLockApplication.class, args);
    }

//    将固定连接生成一个示例，每次使用注入实例即可
    @Bean(initMethod="start",destroyMethod = "close")
    public CuratorFramework getCuratorFramework() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        return client;
    }
}
