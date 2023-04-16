package com.imooc.springcloud;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * EurekaServerApplication: 这是一个注册中心启动类
 * @EnableEurekaServer: 加上这个注解EurekaServerApplication被识别为一个Eureka的注册中心
 * 访问： http://127.0.0.1:20000/
 */

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        // 启动方法，SpringApplicationBuilder构造一个启动类，
        // web: 启动方法
        // run：   调用运行方法
        new SpringApplicationBuilder(EurekaServerApplication.class).
                web(WebApplicationType.SERVLET)
                .run(args);
    }
}
