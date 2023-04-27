package com.imooc.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消费者监听队列
 */

@Component
@Slf4j
public class RabbitMQConsumer {

    /**
     *
     * @param payload    消息的载体
     * @param message    具体消息内容
     */
    @RabbitListener(queues = {RabbitMQConfig.QUEUE_SYS_MSG})
   public  void  watchQueue(String payload, Message message){
        log.info("payload is  "+payload);
        String receivedRoutingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.info("receivedRoutingKey is  "+receivedRoutingKey);
    }
}













