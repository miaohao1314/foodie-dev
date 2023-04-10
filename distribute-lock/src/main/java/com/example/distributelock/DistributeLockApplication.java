package com.example.distributelock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//@MapperScan： 扫描mapper文件
@SpringBootApplication
@MapperScan("com.example.distributelock.dao")
public class DistributeLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributeLockApplication.class, args);
    }

}
