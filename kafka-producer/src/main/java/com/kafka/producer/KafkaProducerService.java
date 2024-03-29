package com.kafka.producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import lombok.extern.slf4j.Slf4j;

/**
 * 	$KafkaProducerService
 * 	第一个创建的类
 * 	类的作用： 用来发送消息的
 * @author Alienware
 *
 */
@Slf4j
@Component
public class KafkaProducerService {


    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     *
     * @param topic    发送的目的地
     * @param object   发送的消息内容
     */
    public void sendMessage(String topic, Object object) {

        // 消费者将消息发送到topic
        // future做一个Callback
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, object);

        // 消息发送成功以后应该去做什么
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("发送消息成功: " + result.toString());
            }

            @Override
            public void onFailure(Throwable throwable) {
                log.error("发送消息失败: " + throwable.getMessage());
            }
        });

    }
}
