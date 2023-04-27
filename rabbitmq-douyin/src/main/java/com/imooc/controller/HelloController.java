package com.imooc.controller;


import com.imooc.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * rabbitmq生产者发送消息
 */

@RestController
@RequestMapping("hello")
public class HelloController {

    //    Springboot和中间件结合，为模板类
    @Autowired
    public RabbitTemplate rabbitTemplate;

    @GetMapping("produce")
    public Object produce() throws Exception {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_MST, "sys.msg.send", "我发了一个消息");
        return "success";
    }
}
