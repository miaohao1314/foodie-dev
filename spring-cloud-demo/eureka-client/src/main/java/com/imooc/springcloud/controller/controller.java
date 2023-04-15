package com.imooc.springcloud.controller;


import com.imooc.springcloud.pojo.Friend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Slf4j: 自动生成logger日志类
 */

@RestController
@Slf4j
public class controller {

    //从配置文件获取值
    @Value("${server.port}")
    private  String port;

    //  对外提供服务的方法
    @GetMapping("sayHello")
    private String sayHello(){
        return "this is "+ port;
    }


    @PostMapping("sayHello")
    private Friend sayHelloPost(@RequestBody Friend friend){
        log.info("you are"+friend.getName());
        friend.setPort(port);
        return  friend;
    }
}
