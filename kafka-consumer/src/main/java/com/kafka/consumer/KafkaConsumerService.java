package com.kafka.consumer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 第二个需要写的内容
 */

@Slf4j
@Component
public class KafkaConsumerService {


    /**
     *
     * 先启动ApplicationTests发送消息，在启动这个类去接收消息
     */

    /**
     *
     * @param record    : 单条消息接收
     * @param acknowledgment
     * @param consumer  ： 接收对应的consumer的消息
     *  启动以后才会创建group02信息
     */
    @KafkaListener(groupId = "group02", topics = "miaohao-topic")
    public void onMessage(ConsumerRecord<String, Object> record, Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
        log.info("消费端接收消息内容是: {}", record.value());
        //	接受完消息以后收工签收机制
        acknowledgment.acknowledge();

        // 启动以后linux控制台输入这行命令进行查看：
        // bin/kafka-consumer-groups.sh --bootstrap-server 10.0.8.6:9092 --describe --group group02
    }
}
